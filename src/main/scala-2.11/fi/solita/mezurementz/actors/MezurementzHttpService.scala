package fi.solita.mezurementz.actors

import java.time.LocalTime
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout
import fi.solita.mezurementz.{HealthStatus, AlarmView}
import fi.solita.mezurementz.messages.{GetAlarms, Alarm}
import spray.http.MediaTypes._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.routing.{ExceptionHandler, HttpService}
import spray.httpx.{SprayJsonSupport}
import spray.util.LoggingContext

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by juhofr on 26/03/16.
  */
class MezurementzHttpService extends Actor with MezurementzMezurementzHttpAPI {

  def actorRefFactory = context
  def receive = runRoute(routes)
}

trait MezurementzMezurementzHttpAPI extends HttpService with SprayJsonSupport {

  import fi.solita.mezurementz.JSONProtocol._

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  val alarmService = actorRefFactory.actorSelection("/user/alarm-service")

  implicit def exceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e: RuntimeException =>
        requestUri { uri =>
          complete(StatusCodes.NotFound, e.getMessage)
        }
    }

  def routes = {
    respondWithMediaType(`application/json`) {
      path("health") {
        complete {
          HealthStatus("OK")
        }
      } ~
      path("alarms") {
          get {
            complete {
              (alarmService ? GetAlarms(""))
                // We use untyped actors, hence mapTo
                .mapTo[Seq[Alarm]]
                // Note that this is map of Future, not collection "in" future
                .map { allAlarms =>
                allAlarms.map { alarm =>
                  AlarmView(alarm.measurement.identifier, alarm.message, alarm.measurement.timestamp)
                }
              }
            }
          }
        } ~
      path("alarms" / Segment) { identifier =>
        get {
          complete {
            (alarmService ? GetAlarms(identifier))
              // We use untyped actors, hence mapTo
              .mapTo[Seq[Alarm]]
              // Note that this is map of Future, not collection "in" future
              .map { allAlarms =>
              allAlarms.map { alarm =>
                AlarmView(alarm.measurement.identifier, alarm.message, alarm.measurement.timestamp)
              }
            }
          }
        }
      }
    }
  }
}

