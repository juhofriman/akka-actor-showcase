package fi.solita.mezurementz.actors

import java.util.concurrent.TimeUnit

import akka.actor.{ActorLogging, Actor}
import akka.util.Timeout
import fi.solita.mezurementz.messages.Measurement

/**
  * Created by juhofr on 26/03/16.
  */
class MeasurementHandler extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Hello! I'm measurement handler")

  override def receive: Receive = {
    case measurement: Measurement =>
      log.info(s"Measurement received l:${measurement.leftValve} r:${measurement.rightValve}")
    case _ => log.info("Received unrecognized message!")
  }
}
