package ui.demo




import model.waitingTime.ForecastMethod
import stream.StreamFactory
import ui.ConfigUtils
import workflow.provider.source.forecaster.ForecasterSourceBuild
import workflow.provider.source.matrix.MCSourceMLE
import workflow.provider.source.sdfa.SDFASourceFromSRE
import workflow.provider.source.wt.{WtSourceDirect, WtSourceMatrix, WtSourceSPST}
import workflow.provider._
import workflow.provider.source.spst.{SPSTSourceDirectI, SPSTSourceFromSDFA}
import workflow.task.engineTask.ERFTask

object overlap extends App {
  final val confidenceThreshold = 0.5
  final val horizon = 50
  final val domain = "maritime"
  final val maxSpread = 5
  final val method = ForecastMethod.CLASSIFY_NEXTK
  final val distance = (0.0001, 1.0)

  final val home = System.getenv("WAYEB_HOME")
  final val dataDir: String = home + "/data/maritime/"
  final val resultsDir: String = home + "/results"

  final val testDatasetFilename: String = dataDir + "ais_brest_syn_227592820.csv"
  final val patternFile: String = home + "/patterns/maritime/port/allenRelWithSRE.sre"
  final val declarationsFile: String = home + "/patterns/maritime/port/declarationsDistance2.sre"

  // First create the training and test stream sources.
  val streamTestSource = StreamFactory.getDomainStreamSource(testDatasetFilename, domain = domain, List.empty)

  // Create a provider for the SDFA
  val sdfap = SDFAProvider(SDFASourceFromSRE(patternFile, ConfigUtils.defaultPolicy, declarationsFile))
  // Wrap a FSM provider around it
  val fsmp = FSMProvider(sdfap)

  // Now execute recognition and forecasting
  val erft = ERFTask(
    fsmp             = fsmp,

    streamSource     = streamTestSource,

  )
  val prof = erft.execute()
  prof.printProfileInfo()


}
