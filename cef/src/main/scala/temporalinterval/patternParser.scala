package temporalinterval


import scala.util.parsing.combinator._

case class Pattern(
                    outputDefinition: String,
                    inputStream: String,
                    partitionBy: Option[List[String]],
                    intervalEventDefinitions: List[IntervalEventDefinition],
                    compoundPattern: CompoundPattern,
                    timeValue: String
                  )

case class IntervalEventDefinition(predicate: BooleanExpression, duration: Option[Duration])
case class Duration(timeConstraint: TimeConstraint)
sealed trait TimeConstraint
case class AtLeast(timeValue: String) extends TimeConstraint
case class AtMost(timeValue: String) extends TimeConstraint
case class Between(timeValue1: String, timeValue2: String) extends TimeConstraint

sealed trait BooleanExpression
case class Predicate(attribute: String, relation: Relation, constant: Constant) extends BooleanExpression
case class And(expr1: BooleanExpression, expr2: BooleanExpression) extends BooleanExpression
case class Or(expr1: BooleanExpression, expr2: BooleanExpression) extends BooleanExpression
case class Not(expr: BooleanExpression) extends BooleanExpression

sealed trait Relation
case object GreaterThan extends Relation
case object LessThan extends Relation
case object EqualTo extends Relation
case object GreaterThanOrEqualTo extends Relation
case object LessThanOrEqualTo extends Relation

sealed trait Constant
case class NumberConstant(value: Int) extends Constant
case class StringConstant(value: String) extends Constant

sealed trait CompoundPattern
case class BasicPattern(allenConstraint: AllenConstraint, rest: Option[BasicPattern]) extends CompoundPattern
case class OrPattern(pattern1: CompoundPattern, pattern2: CompoundPattern) extends CompoundPattern

case class AllenConstraint(event1: IntervalEventDefinition, relation: AllenRelation, event2: IntervalEventDefinition)
sealed trait AllenRelation
case object Before extends AllenRelation
case object After extends AllenRelation
case object Meets extends AllenRelation
case object MetBy extends AllenRelation
case object Overlaps extends AllenRelation
case object OverlappedBy extends AllenRelation
case object Starts extends AllenRelation
case object StartedBy extends AllenRelation
case object Finishes extends AllenRelation
case object FinishedBy extends AllenRelation
case object During extends AllenRelation
case object Contains extends AllenRelation
case object Equals extends AllenRelation

class PatternParser extends JavaTokenParsers {

  private def preprocessInput(input: String): String = {
    val singleLineInput = input.replaceAll("\n", " ").replaceAll("\r", " ")
    val keywords = List("SELECT", "FROM", "PARTITION BY", "DEFINE", "PATTERN", "WITHIN")
    val processedInput = keywords.foldLeft(singleLineInput) { (acc, keyword) =>
      acc.replaceAll("(?i)" + keyword, keyword)
    }
    val occurrences = keywords.map { keyword =>
      val count = ("(?i)" + keyword).r.findAllMatchIn(processedInput).size
      (keyword, count)
    }.toMap
    if (occurrences("PARTITION BY") > 1) {
      throw new IllegalArgumentException("\"PARTITION BY\" can only appear 0 or 1 times.")
    }
    val singleOccurrenceKeywords = List("SELECT", "FROM", "DEFINE", "PATTERN", "WITHIN")
    singleOccurrenceKeywords.foreach { keyword =>
      if (occurrences(keyword) != 1) {
        throw new IllegalArgumentException(s""""$keyword" must appear exactly once.""")
      }
    }
    val inputWithNewlines = keywords.foldLeft(processedInput) { (acc, keyword) =>
      acc.replaceAll("(?i)" + keyword, "\n" + keyword)
    }
    inputWithNewlines.split("\n").tail.mkString("\n")
  }

  def identifier: Parser[String] = """[a-zA-Z_]\w*""".r

  def outputDefinition: Parser[String] = "SELECT" ~> ".+".r

  def inputStream: Parser[String] = "FROM" ~> identifier

  def partitionBy: Parser[List[String]] = "PARTITION BY" ~> repsep(identifier, ",")

  def timeValue: Parser[String] = """\d+[smh]""".r

  def duration: Parser[Duration] = (
    "AT-LEAST" ~> timeValue ^^ { tv => Duration(AtLeast(tv)) }
      | "AT-MOST" ~> timeValue ^^ { tv => Duration(AtMost(tv)) }
      | "BETWEEN" ~ timeValue ~ "AND" ~ timeValue ^^ { case _ ~ tv1 ~ _ ~ tv2 => Duration(Between(tv1, tv2)) }
    )

  def constant: Parser[Constant] = (
    wholeNumber ^^ { n => NumberConstant(n.toInt) }
      | stringLiteral ^^ { s => StringConstant(s.stripPrefix("\"").stripSuffix("\"")) }
    )

  def relation: Parser[Relation] = (
    ">" ^^ { _ => GreaterThan }
      | "<" ^^ { _ => LessThan }
      | "=" ^^ { _ => EqualTo }
      | ">=" ^^ { _ => GreaterThanOrEqualTo }
      | "<=" ^^ { _ => LessThanOrEqualTo }
    )

  def predicate: Parser[Predicate] = identifier ~ relation ~ constant ^^ {
    case attr ~ rel ~ const => Predicate(attr, rel, const)
  }

  def booleanExpression: Parser[BooleanExpression] = (
    predicate
      | booleanExpression ~ "AND" ~ booleanExpression ^^ { case expr1 ~ _ ~ expr2 => And(expr1, expr2) }
      | booleanExpression ~ "OR" ~ booleanExpression ^^ { case expr1 ~ _ ~ expr2 => Or(expr1, expr2) }
      | "NOT" ~ booleanExpression ^^ { case _ ~ expr => Not(expr) }
      | "(" ~> booleanExpression <~ ")"
    )

  def intervalEvent: Parser[IntervalEventDefinition] = booleanExpression ~ opt(duration) ^^ {
    case expr ~ dur => IntervalEventDefinition(expr, dur)
  }

  def intervalEventDefinitions: Parser[List[IntervalEventDefinition]] = "DEFINE" ~> rep1sep(intervalEvent, ",")

  def allenRelation: Parser[AllenRelation] = (
    "before" ^^ { _ => Before }
      | "after" ^^ { _ => After }
      | "meets" ^^ { _ => Meets }
      | "met-by" ^^ { _ => MetBy }
      | "overlaps" ^^ { _ => Overlaps }
      | "overlapped-by" ^^ { _ => OverlappedBy }
      | "starts" ^^ { _ => Starts }
      | "started-by" ^^ { _ => StartedBy }
      | "finishes" ^^ { _ => Finishes }
      | "finished-by" ^^ { _ => FinishedBy }
      | "during" ^^ { _ => During }
      | "contains" ^^ { _ => Contains }
      | "equals" ^^ { _ => Equals }
    )

  def allenConstraint: Parser[AllenConstraint] = intervalEvent ~ allenRelation ~ intervalEvent ^^ {
    case event1 ~ rel ~ event2 => AllenConstraint(event1, rel, event2)
  }

  def basicPattern: Parser[CompoundPattern] = allenConstraint ~ opt("AND" ~> basicPattern) ^^ {
    case constraint ~ rest => BasicPattern(constraint, rest)
  }

  def compoundPattern: Parser[CompoundPattern] = (
    basicPattern
      | compoundPattern ~ "OR" ~ compoundPattern ^^ { case pattern1 ~ _ ~ pattern2 => OrPattern(pattern1, pattern2) }
    )

  def pattern: Parser[Pattern] = (
    outputDefinition ~
      inputStream ~
      opt(partitionBy) ~
      intervalEventDefinitions ~
      "PATTERN" ~> compoundPattern ~
      "WITHIN" ~> timeValue
    ) ^^ {
    case output ~ input ~ partition ~ definitions ~ pattern ~ within =>
      Pattern(output, input, partition, definitions, pattern, within)
  }


  def parsePattern(input: String): Pattern = {
    val preprocessedInput = preprocessInput(input)
    parseAll(pattern, preprocessedInput) match {
      case Success(result, _) => result
      case NoSuccess(msg, _) => throw new IllegalArgumentException(s"Failed to parse pattern: $msg")
    }
  }
}
object PatternParserTest extends App {
  val inputPattern = """
        SELECT input
        FROM eventStreamPartition
        PARTITION BY userId
        DEFINE  A=speed>20,
                B=speed>20,
        PATTERN A before B
        WITHIN 10m
      """

  val parser = new PatternParser

  try {
    val result = parser.parsePattern(inputPattern)
    println(s"Parsing successful: $result")
  } catch {
    case ex: IllegalArgumentException =>
      println(s"Parsing failed: ${ex.getMessage}")
  }
}