package stream.domain.linearRoad


import com.typesafe.scalalogging.LazyLogging
import stream.source.LineParser
import stream.{GenericEvent, ResetEvent}
// Sample line:
//VID,SPEED,ACCEL,XWay,Lane,Dir,Seg,Pos
//116,32,0.00,0,0,0,2,10580
//103,30,0.00,0,0,0,4,21157
object linearRoadLineParser extends LineParser with LazyLogging {

  override def line2Event(
                           line: Seq[String],
                           id: Int
                         ): GenericEvent = {
    try {
//      val timestamp = line(0).toLong
//      这里用id代替timestamp
      val vid = line(0).toInt
      val speed = line(1).toInt
      val accel = line(2).toDouble
      val xway = line(3).toInt
      val lane = line(4).toByte
      val dir = line(5).toByte
      val seg = line(6).toByte
      val pos = line(7).toInt

      val ge = GenericEvent(id, "SampleLineRoad", id,
        Map("vid" -> vid, "speed" -> speed, "accel" -> accel, "xway" -> xway, "lane" -> lane,
          "dir" -> dir, "seg" -> seg, "pos" -> pos))
      ge
    } catch {
      case e: Exception => {
        logger.warn("COULD NOT PARSE LINE " + line)
        throw new Error
      }
    }
  }
}
