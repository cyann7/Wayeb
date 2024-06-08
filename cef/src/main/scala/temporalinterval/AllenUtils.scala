package temporalinterval

object AllenUtils {

  def combinePosAWithPosB(predicateA: String, predicateB: String): String = {
    val res=s"^($predicateA,$predicateB)"
    res
  }

  def combinePosAWithNegB(predicateA: String, predicateB: String): String = {
    s"^($predicateA,-$predicateB)"
  }

  def combineNegAWithPosB(predicateA: String, predicateB: String): String = {
    s"^(-$predicateA,$predicateB)"
  }

  def combineNegAWithNegB(predicateA: String, predicateB: String): String = {
    s"^(-$predicateA,-$predicateB)"
  }
}
