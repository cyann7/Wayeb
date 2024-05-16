package ui.demo

import java.io._
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

object myapp extends App {
  final val confidenceThreshold = 0.5
  final val horizon = 50
  final val domain = "maritime"
  final val maxSpread = 5
  final val method = ForecastMethod.CLASSIFY_NEXTK
  final val distance = (0.0001, 1.0)

  //  final val home = System.getenv("WAYEB_HOME")
  final val home: String =("/Users/czq/Code/Wayeb")
  final val dataDir: String = home + "/data/maritime/"
  final val resultsDir: String = home + "/results"
//  final val testDatasetFilename: String = dataDir + "227592820.csv"
  final val trainDatasetFilename: String = dataDir + "227592820.csv"
  final val patternFile: String = home + "/patterns/maritime/port/pattern.sre"
  final val declarationsFile: String = home + "/patterns/maritime/port/declarationsDistance1.sre"


  // Create a provider for the SDFA
  val sdfap = SDFAProvider(SDFASourceFromSRE(patternFile, ConfigUtils.defaultPolicy, declarationsFile))
  // Wrap a FSM provider around it
  val fsmp = FSMProvider(sdfap)

  // 获取目录下的所有文件名
  final val csvhome: String =("/Users/czq/Code/ais_brest_synopses_v0.8/processed-loc/v")
//  val fileList = new File(csvhome).listFiles.filter(_.isFile)
  val fileList = new File(csvhome).listFiles.filter(_.getName.toLowerCase.endsWith(".csv"))
  // 遍历文件列表并处理每个文件
  val matchMap: Map[String, Int] = fileList.map { file =>
    val matchNo = getMatchesNo(file.getName)
    (file.getName, matchNo)
  }.toMap


  val resFile = new File("/Users/czq/Code/ais_brest_synopses_v0.8/processed-loc/res.txt")
  val resWriter = new PrintWriter(new BufferedWriter(new FileWriter(resFile)))
  matchMap.foreach { case (filename, matchNo) =>
    val line = s"Filename: $filename, Matches: $matchNo"
    println(line)
    resWriter.println(line)
  }


  def getMatchesNo(filename: String): Int = {

    val testDatasetFilename: String =csvhome+"/"+filename
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




  //  val f1score = prof.getStatFor("f1", 0)
  //  println("\n\n\n\n\n\tF1-score: " + f1score)




  //
  //  final val pMin = 0.001
  //  final val alpha = 0.0
  //  final val gamma = 0.001
  //  final val r = 1.05
  //  val spstp1 = SPSTProvider(SPSTSourceFromSDFA(sdfap, 1, streamTrainSource, pMin = pMin, alpha = alpha, gamma = gamma, r = r))
  //  val spstp = SPSTProvider(SPSTSourceDirectI(List(spstp1.provide().head)))
  //  val fsmp1 = FSMProvider(spstp)
  //  val wtp0 = WtProvider(WtSourceSPST(
  //    spstp,
  //    horizon         = horizon,
  //    cutoffThreshold = ConfigUtils.wtCutoffThreshold,
  //    distance        = distance
  //  ))
  //  val wtp1 = WtProvider(WtSourceDirect(List(wtp0.provide().head)))
  //  val pp1 = ForecasterProvider(ForecasterSourceBuild(
  //    fsmp1,
  //    wtp1,
  //    horizon             = horizon,
  //    confidenceThreshold = confidenceThreshold,
  //    maxSpread           = maxSpread,
  //    method              = method
  //  ))
  //
  //  val erft1 = ERFTask(
  //    fsmp             = fsmp1,
  //    pp               = pp1,
  //    predictorEnabled = true,
  //    finalsEnabled    = false,
  //    expirationDeadline   = ConfigUtils.defaultExpiration,
  //    distance         = distance,
  //    streamSource     = streamTestSource
  //  )
  //  val prof1 = erft1.execute()
  //  prof1.printProfileInfo()
  //
  //  val f1score1 = prof1.getStatFor("f1", 0)
  //  println("\n\n\n\n\n\tF1-score: " + f1score1)
  //
}
