package temporalinterval.allenrelations

import fsm.symbolic.logic.Predicate
import fsm.symbolic.logic.predicates
import temporalinterval.AllenRelation
import temporalinterval.AllenUtils


/**
 * Class representing the "before" Allen relation.
 *
 * @param A The first predicate.
 * @param B The second predicate.
 */
case class BEFORE(predicateA:String, predicateB:String)  extends AllenRelation(predicateA:String,predicateB:String)  {

  /**
   * Converts two predicates to an SRE pattern for "before" relation.
   *
   * @param predicateA The first predicate.
   * @param predicateB The second predicate.
   * @return The SRE pattern string.
   */
  override def strPredicates2SREPattern : String = {
    val r1=predicateA
    val r2=AllenUtils.combineNegAWithNegB(predicateA,predicateB)
    val r3=predicateB
    val res=";("+
      "*("+r1+"),"+
      r1+","+
      "*("+r2+"),"+
      r2+","+
      "*("+r3+"),"+
      r3+")"
//    println(res)
    res
  }
}

