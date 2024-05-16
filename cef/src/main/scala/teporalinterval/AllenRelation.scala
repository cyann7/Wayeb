package teporalinterval



import fsm.symbolic.logic.Predicate

/**
 * Abstract class representing an Allen relation.
 *
 * @param A The first predicate.
 * @param B The second predicate.
 */
abstract class AllenRelation(val A: Predicate, val B: Predicate) extends Serializable {

  /**
   * Converts two predicates to an SRE pattern.
   *
   * @param predicateA The first predicate.
   * @param predicateB The second predicate.
   * @return The SRE pattern string.
   */
  def toSREPattern(predicateA: Predicate, predicateB: Predicate): String
}