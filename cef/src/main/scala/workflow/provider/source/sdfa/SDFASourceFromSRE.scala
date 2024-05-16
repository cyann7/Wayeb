package workflow.provider.source.sdfa

import fsm.CountPolicy.CountPolicy
import ui.ConfigUtils

object SDFASourceFromSRE {
  // 构造函数1，带有四个参数
  def apply(
             sreFile: String,
             policy: CountPolicy,
             declarations: String,
             minTermMethod: String
           ): SDFASourceFromSRE = new SDFASourceFromSRE(sreFile, policy, declarations, minTermMethod)
// 构造函数2，带有3个参数，第四个参数使用了一个默认的值，这个默认值通过 "ConfigUtils" 对象的 "defaultMinTermMethod" 属性获得
  def apply(
             sreFile: String,
             policy: CountPolicy,
             declarations: String
           ): SDFASourceFromSRE = new SDFASourceFromSRE(sreFile, policy, declarations, ConfigUtils.defaultMinTermMethod)
}

// 用以下的类来进行构建
class SDFASourceFromSRE(
                         val sreFile: String,
                         val policy: CountPolicy,
                         val declarations: String,
                         val minTermMethod: String
                       ) extends SDFASource {

}
