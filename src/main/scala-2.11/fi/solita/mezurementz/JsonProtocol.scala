package fi.solita.mezurementz

import java.time.LocalTime

import spray.json.{JsString, JsValue, JsonFormat, DefaultJsonProtocol}

/**
  * Created by juhofr on 28/03/16.
  */
object JSONProtocol extends DefaultJsonProtocol {

  implicit object LocalTimeJsonFormat extends JsonFormat[LocalTime] {
    override def write(obj: LocalTime): JsValue = JsString(obj.toString)
    override def read(json: JsValue): LocalTime = LocalTime.parse(json.convertTo[String])
  }

  implicit val healthStatusFormat = jsonFormat1(HealthStatus)
  implicit val alarmViewFormat = jsonFormat3(AlarmView)
}

case class HealthStatus(status: String)
case class AlarmView(identifier: String, message: String, timestamp: LocalTime)