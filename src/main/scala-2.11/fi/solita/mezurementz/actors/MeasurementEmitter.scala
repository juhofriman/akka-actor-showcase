package fi.solita.mezurementz.actors

import java.time.{Instant, LocalTime}
import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import fi.solita.mezurementz.messages.Measurement

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by juhofr on 26/03/16.
  */
class MeasurementEmitter(identifier: String, delay: FiniteDuration, interval: FiniteDuration) extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Measurement emitter emits measurements")

  val random = scala.util.Random

  def generateMeasurement(): Measurement = {
    Measurement(identifier, Instant.now(), random.nextLong, random.nextLong)
  }

  context.system.scheduler.schedule(delay, interval) {
    log.info("--------")
    log.info("Emitting measurement")
    // Just fire new "measurement" to system
    context.actorSelection("/user/measurement-handler") ! generateMeasurement()
  }

  override def receive: Receive = {
    case _ => log.info("Received message! (This was quite unexpected!)")
  }
}
