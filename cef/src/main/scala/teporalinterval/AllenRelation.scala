package temporalinterval

import fsm.symbolic.logic.Predicate

/**
 * Abstract class representing an Allen relation.
 */
abstract class AllenRelation(predicateA:String,predicateB:String)  extends Serializable {

  def strPredicates2SREPattern : String
}