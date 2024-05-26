import org.scalatest.{FlatSpec, Matchers}
import org.scalatestplus.junit.JUnitRunner
import org.junit.runner.RunWith
import temporalinterval.AllenUtils

@RunWith(classOf[JUnitRunner])
class AllenUtilsTest extends FlatSpec with Matchers {

  "combinePosAWithPosB" should "return the correct SRE pattern for positive A and positive B" in {
    val predA = "GT(speed,15.0)"
    val predB = "LT(heading,95.0)"
    val result = AllenUtils.combinePosAWithPosB(predA, predB)
    result.replaceAll("\\s+", "") should be ("^( GT(speed,15.0) , LT(heading,95.0) )".replaceAll("\\s+", ""))
  }

  "combinePosAWithNegB" should "return the correct SRE pattern for positive A and negative B" in {
    val predA = "GT(speed,15.0)"
    val predB = "LT(heading,95.0)"
    val result = AllenUtils.combinePosAWithNegB(predA, predB)
    result.replaceAll("\\s+", "") should be ("^( GT(speed,15.0) , - LT(heading,95.0) )".replaceAll("\\s+", ""))
  }

  "combineNegAWithPosB" should "return the correct SRE pattern for negative A and positive B" in {

    val predA = "GT(speed,15.0)"
    val predB = "LT(heading,95.0)"
    val result = AllenUtils.combineNegAWithPosB(predA, predB)
    result.replaceAll("\\s+", "") should be ("^( - GT(speed,15.0) , LT(heading,95.0) )".replaceAll("\\s+", ""))
  }

  "combineNegAWithNegB" should "return the correct SRE pattern for negative A and negative B" in {

    val predA = "GT(speed,15.0)"
    val predB = "LT(heading,95.0)"
    val result = AllenUtils.combineNegAWithNegB(predA, predB)
    result.replaceAll("\\s+", "") should be ("^( - GT(speed,15.0) , - LT(heading,95.0) )".replaceAll("\\s+", ""))
  }
}
