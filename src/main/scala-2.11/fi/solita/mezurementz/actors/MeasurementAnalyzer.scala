package fi.solita.mezurementz.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import fi.solita.mezurementz.messages.{PleaseAnalyzeThis, AnalyzedMeasurement, Measurement}

/**
  * Created by juhofr on 26/03/16.
  */
class MeasurementAnalyzer extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Hello! I'm measurement analyzer")

  override def receive: Receive = {
    case data: PleaseAnalyzeThis => {
      if(data.measurement.leftValve < 0 && data.measurement.rightValve < 0) {
        sender() ! AnalyzedMeasurement(data.measurement, "BOTH VALVES NEGATIVE", true)
      } else {
        sender() ! AnalyzedMeasurement(data.measurement, "OK", false)
      }
    }

    case _ => log.info("Received unrecognized message!")
  }
}
