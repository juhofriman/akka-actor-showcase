package fi.solita.mezurementz.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import fi.solita.mezurementz.messages.{PleaseAnalyzeThis, AnalyzedMeasurement, Measurement}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by juhofr on 26/03/16.
  */
class MeasurementAnalyzer extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Hello! I'm measurement analyzer")

  override def receive: Receive = {
    case data: PleaseAnalyzeThis => {
      // Don't use Thread.sleep! It breaks things seriously
      if(data.measurement.leftValve < 0 && data.measurement.rightValve < 0) {
        context.system.scheduler.scheduleOnce(100 milliseconds, sender(), AnalyzedMeasurement(data.measurement, "BOTH VALVES NEGATIVE", true))
      } else {
        context.system.scheduler.scheduleOnce(100 milliseconds, sender(), AnalyzedMeasurement(data.measurement, "OK", false))
      }
    }

    case _ => log.info("Received unrecognized message!")
  }
}
