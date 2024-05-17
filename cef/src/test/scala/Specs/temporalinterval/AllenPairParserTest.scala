package temporalinterval

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import temporalinterval.allenrelations._
import temporalinterval.AllenPairParser

@RunWith(classOf[JUnitRunner])
class AllenPairParserTest extends FlatSpec with Matchers {

  "OVERLAP" should "correctly parse input and generate SRE pattern" in {

    val input = "OVERLAP(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(GT(speed,15.0),-LT(heading,95.0))),^(GT(speed,15.0),-LT(heading,95.0)),*(^(GT(speed,15.0)," +
      "LT(heading,95.0))),^(GT(speed,15.0),LT(heading,95.0)),*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "iOVERLAP" should "correctly parse input and generate SRE pattern" in {

    val input = "iOVERLAP(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(LT(heading,95.0),-GT(speed,15.0))),^(LT(heading,95.0),-GT(speed,15.0)),*(^(LT(heading,95.0)," +
      "GT(speed,15.0))),^(LT(heading,95.0),GT(speed,15.0)),*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "START" should "correctly parse input and generate SRE pattern" in {

    val input = "START(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-GT(speed,15.0),-LT(heading,95.0))),^(-GT(speed,15.0),-LT(heading,95.0)),*(^(GT(speed,15.0)," +
      "LT(heading,95.0))),^(GT(speed,15.0),LT(heading,95.0)),*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }


  "DURING" should "correctly parse input and generate SRE pattern" in {

    val input = "DURING(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0)),*(^(GT(speed,15.0)," +
      "LT(heading,95.0))),^(GT(speed,15.0),LT(heading,95.0)),*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "FINISH" should "correctly parse input and generate SRE pattern" in {

    val input = "FINISH(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0)),*(^(GT(speed,15.0)," +
      "LT(heading,95.0))),^(GT(speed,15.0),LT(heading,95.0)),*(^(-GT(speed,15.0),-LT(heading,95.0))),^(-GT(speed,15.0),-LT(heading,95.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "BEFORE" should "correctly parse input and generate SRE pattern" in {

    val input = "BEFORE(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(" +
      "*( GT(speed,15.0)),GT(speed,15.0)," +
      "*(^(-GT(speed,15.0),-LT(heading,95.0))),^(-GT(speed,15.0),-LT(heading,95.0))," +
      "*(LT(heading,95.0)),LT(heading,95.0)" +
      ")"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "MEET" should "correctly parse input and generate SRE pattern" in {

    val input = "MEET(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(" +
      "*(^(GT(speed,15.0),-LT(heading,95.0))),^(GT(speed,15.0),-LT(heading,95.0))," +
      "*(^(-GT(speed,15.0),LT(heading,95.0))),^(-GT(speed,15.0),LT(heading,95.0))" +
      ")"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "EQUAL" should "correctly parse input and generate SRE pattern" in {

    val input = "EQUAL(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(" +
      "*(^(-GT(speed,15.0),-LT(heading,95.0))),^(-GT(speed,15.0),-LT(heading,95.0))," +
      "*(^(GT(speed,15.0),LT(heading,95.0))),^(GT(speed,15.0),LT(heading,95.0))," +
      "*(^(-GT(speed,15.0),-LT(heading,95.0))),^(-GT(speed,15.0),-LT(heading,95.0))" +
      ")"
    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "iSTART" should "correctly parse input and generate SRE pattern" in {

    val input = "iSTART(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-LT(heading,95.0),-GT(speed,15.0))),^(-LT(heading,95.0),-GT(speed,15.0)),*(^(LT(heading,95.0)," +
      "GT(speed,15.0))),^(LT(heading,95.0),GT(speed,15.0)),*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }

  "iDURING" should "correctly parse input and generate SRE pattern" in {

    val input = "iDURING(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0)),*(^(LT(heading,95.0)," +
      "GT(speed,15.0))),^(LT(heading,95.0),GT(speed,15.0)),*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "iFINISH" should "correctly parse input and generate SRE pattern" in {

    val input = "iFINISH(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0)),*(^(LT(heading,95.0)," +
      "GT(speed,15.0))),^(LT(heading,95.0),GT(speed,15.0)),*(^(-LT(heading,95.0),-GT(speed,15.0))),^(-LT(heading,95.0),-GT(speed,15.0)))"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "iBEFORE" should "correctly parse input and generate SRE pattern" in {

    val input = "iBEFORE(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(" +
      "*( LT(heading,95.0)),LT(heading,95.0)," +
      "*(^(-LT(heading,95.0),-GT(speed,15.0))),^(-LT(heading,95.0),-GT(speed,15.0))," +
      "*(GT(speed,15.0)),GT(speed,15.0)" +
      ")"

    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
  "iMEET" should "correctly parse input and generate SRE pattern" in {

    val input = "iMEET(GT(speed,15.0),LT(heading,95.0))"
    val srePattern = AllenPairParser.parse(input)
    val groundTruth=";(" +
      "*(^(LT(heading,95.0),-GT(speed,15.0))),^(LT(heading,95.0),-GT(speed,15.0))," +
      "*(^(-LT(heading,95.0),GT(speed,15.0))),^(-LT(heading,95.0),GT(speed,15.0))" +
      ")"
    srePattern.replaceAll("\\s+", "") should be(groundTruth.replaceAll("\\s+", ""))
  }
}
