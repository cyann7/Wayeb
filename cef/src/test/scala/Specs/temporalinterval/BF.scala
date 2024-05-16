package Specs.temporalinterval

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatestplus.junit.JUnitRunner
import teporalinterval.allenrelations.BF
import fsm.symbolic.logic.predicates.{GT, OutsideCirclePredicate}

@RunWith(classOf[JUnitRunner])
class BF  extends FlatSpec {

  "BF.toSREPattern" should "return correct SRE pattern" in {
    // 创建测试数据
    val predicateA = GT
    val predicateB =  OutsideCirclePredicate
    val bf = BF(predicateA, predicateB)

    // 调用方法进行测试
    val result = bf.toSREPattern()

    // 断言测试结果是否符合预期
    assert(result == /* 期望的 SRE pattern */)
  }
}
