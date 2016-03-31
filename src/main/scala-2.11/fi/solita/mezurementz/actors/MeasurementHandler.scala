package fi.solita.mezurementz.actors

import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.{AtomicLong, AtomicInteger}

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

  val measurementCount = new AtomicInteger(0)
  val measurementsAnalyzedCount = new AtomicInteger(0)
  val alarmingMeasurementsCount = new AtomicInteger(0)
  val sumOfProcessingTimes = new AtomicLong(0)
  val hasAlarms = scala.collection.mutable.Set[String]()

  override def receive: Receive = {
    case measurement: Measurement =>
      log.info(s"Measurement received l:${measurement.leftValve} r:${measurement.rightValve}")
      measurementCount.incrementAndGet()
      // Ask measurement-analyzer to analyze measurement
      // Note that ask returns Future[Any], hence .mapTo[AnalyzedMeasurement]
      // Typed actors do exist as well
      val analyzationResult =
        (context.actorSelection("/user/measurement-analyzer") ? PleaseAnalyzeThis(measurement))
          .mapTo[AnalyzedMeasurement]

      // Future callback using map
      analyzationResult.map { analyzationResult =>
        log.info("Received anylzation")
        measurementsAnalyzedCount.incrementAndGet()
        if(analyzationResult.isAlarm) {
          // Trigger alarm in the system
          context.actorSelection("/user/alarm-service") ! Alarm(analyzationResult.measurement, analyzationResult.payload)
          alarmingMeasurementsCount.incrementAndGet()
          hasAlarms.add(analyzationResult.measurement.identifier)
        }
        sumOfProcessingTimes.addAndGet(Instant.now().toEpochMilli - measurement.timestamp.toEpochMilli)
      }

    case strCommand: String =>
      sender() ! Map[String, String](
        "measurementsReceived" -> measurementCount.get().toString,
        "measurementsProcessed" -> measurementsAnalyzedCount.get().toString,
        "alarmingMeasurements" -> alarmingMeasurementsCount.get().toString,
        "averageMeasurementProcessingTime" -> (sumOfProcessingTimes.get().toFloat / measurementsAnalyzedCount.get()).toString,
        "hasAlarms" -> hasAlarms.mkString(","))

    case _ => log.info("Received unrecognized message!")
  }
}
