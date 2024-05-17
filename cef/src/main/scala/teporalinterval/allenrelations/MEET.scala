package temporalinterval.allenrelations

import fsm.symbolic.logic.Predicate
import fsm.symbolic.logic.predicates
import temporalinterval.AllenRelation
import temporalinterval.AllenUtils


/**
 * Class representing the "meet" Allen relation.
 *
 * @param A The first predicate.
 * @param B The second predicate.
 */
case class MEET(predicateA: String, predicateB: String) extends AllenRelation(predicateA: String, predicateB: String) {

  /**
   * Converts two predicates to an SRE pattern for "meet" relation.
   *
   * @param predicateA The first predicate.
   * @param predicateB The second predicate.
   * @return The SRE pattern string.
   */
  override def strPredicates2SREPattern: String = {
    val r1 = AllenUtils.combinePosAWithNegB(predicateA, predicateB)
    val r3 = AllenUtils.combineNegAWithPosB(predicateA, predicateB)
    val res = ";(" +
      "*(" + r1 + ")," +
      r1 + "," +
      "*(" + r3 + ")," +
      r3 + ")"
    //    println(res)
    res
  }
}

