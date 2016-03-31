package fi.solita.mezurementz.actors

import java.util.concurrent.TimeUnit

import akka.pattern.ask
import akka.actor.{ActorLogging, Actor}
import akka.util.Timeout
import fi.solita.mezurementz.messages.{Alarm, AnalyzedMeasurement, PleaseAnalyzeThis, Measurement}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by juhofr on 26/03/16.
  */
class MeasurementHandler extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Hello! I'm measurement handler")

  override def receive: Receive = {
    case measurement: Measurement =>
      log.info(s"Measurement received l:${measurement.leftValve} r:${measurement.rightValve}")

      // Ask measurement-analyzer to analyze measurement
      // Note that ask returns Future[Any], hence .mapTo[AnalyzedMeasurement]
      // Typed actors do exist as well
      val analyzationResult =
        (context.actorSelection("/user/measurement-analyzer") ? PleaseAnalyzeThis(measurement))
          .mapTo[AnalyzedMeasurement]

      // Future callback using map
      analyzationResult.map { analyzationResult =>
        if(analyzationResult.isAlarm) {
          // Trigger alarm in the system
          context.actorSelection("/user/alarm-service") ! Alarm(analyzationResult.measurement, analyzationResult.payload)
        }
      }

    case _ => log.info("Received unrecognized message!")
  }
}
