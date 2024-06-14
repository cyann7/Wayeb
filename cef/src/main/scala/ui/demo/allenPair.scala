package ui.demo

import java.io._
import java.nio.file.Paths
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
import java.io.File

object allenPair extends App {
  //CONFIG
  final val domain = "linearRoad"
  final val datasetFilename = "1m_linear_accel.csv"
  final val patternFileName= "overlap(speed,dec).sre"
  final val home = Paths.get(System.getenv("WAYEB_HOME"))


  //DEFAULT CONFIG
  final val dataDir = home.resolve("data").resolve(domain)
  final val DatasetPath  = dataDir.resolve(datasetFilename)
  final val patternFile  = home.resolve("patterns").resolve(domain).resolve(patternFileName)
  final val resFileName=s"res_${java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))}.txt"
  final val resFilePath = home.resolve("results").resolve(domain).resolve(resFileName)

//  final val declarationsFile: String = home + "/patterns/maritime/port/declarationsDistance1.sre"





  // Create a provider for the SDFA
  val sdfap = SDFAProvider(SDFASourceFromSRE(patternFile.toString(), ConfigUtils.defaultPolicy, ""))
  // Wrap a FSM provider around it
  val fsmp = FSMProvider(sdfap)


  def getMatchesNo(): Int = {

    val testDatasetFilename=DatasetPath.toString()
    val streamTestSource = StreamFactory.getDomainStreamSource(testDatasetFilename, domain = domain, List.empty)
    // Now execute recognition and forecasting
    val erft = ERFTask(
      fsmp             = fsmp,
      //    pp               = pp,
      //    predictorEnabled = true,
      //    finalsEnabled    = false,
      //    expirationDeadline   = ConfigUtils.defaultExpiration,
      //    distance         = distance,
      streamSource     = streamTestSource,
      //    collectStats = true,
      //    show = true,
      //    reset=true,
    )
    val prof = erft.execute()
    //  prof.printProfileInfo()
    val matchesNo =prof.getStat("matchesNo").toInt
    matchesNo
  }
  val matchNo=getMatchesNo()
  println(s"MatchesNo: $matchNo")
}
