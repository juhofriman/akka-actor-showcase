package fi.solita.mezurementz

import java.time.{Instant, LocalTime}

import spray.json.{JsString, JsValue, JsonFormat, DefaultJsonProtocol}

/**
  * Created by juhofr on 28/03/16.
  */
object JSONProtocol extends DefaultJsonProtocol {

  implicit object InstantJsonFormat extends JsonFormat[Instant] {
    override def write(obj: Instant): JsValue = JsString(obj.toString)
    override def read(json: JsValue): Instant = Instant.now()
  }

  implicit val healthStatusFormat = jsonFormat2(HealthStatus)
  implicit val alarmViewFormat = jsonFormat3(AlarmView)
}

case class HealthStatus(status: String, statistics: Map[String, String])
case class AlarmView(identifier: String, message: String, timestamp: Instant)