package temporalinterval

import temporalinterval.allenrelations._

object AllenPair2SRE {
  def parse(input: String): String = {
    val (relationType, strPredicateAWithArgs, strPredicateBWithArgs) = parseInput(input)

    relationType match {
      case "OVERLAP" => OVERLAP(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iOVERLAP" => OVERLAP(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "START" => START(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iSTART" => START(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "DURING" => DURING(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iDURING" => DURING(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "FINISH" => FINISH(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iFINISH" => FINISH(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "BEFORE" => BEFORE(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iBEFORE" => BEFORE(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "MEET" => MEET(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern
      case "iMEET" => MEET(strPredicateBWithArgs,strPredicateAWithArgs).strPredicates2SREPattern
      case "EQUAL" => EQUAL(strPredicateAWithArgs, strPredicateBWithArgs).strPredicates2SREPattern


      case _ => throw new IllegalArgumentException("Unknown Allen relation type")
    }
  }

  private def parseInput(input: String): (String, String, String) = {
    val relationType = input.takeWhile(_ != '(')
    val innerContent = input.stripPrefix(s"$relationType(").stripSuffix(")")
    val strPredicateAWithArgs = innerContent.split("\\),")(0)+")"
    val strPredicateBWithArgs = innerContent.split("\\),")(1)
    (relationType, strPredicateAWithArgs, strPredicateBWithArgs)
  }
  def main(args: Array[String]): Unit = {
//    "OVERLAP(GT(speed,15.0),LT(heading,95.0))"
    val input = "OVERLAP(GTE(speed,63.0),GTE(accel,-0.00455))"
    val result = parse(input)
    println(s"Input: $input")
    println(s"Result: $result")
  }
}


