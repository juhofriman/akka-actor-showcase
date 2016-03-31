package fi.solita.mezurementz

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import fi.solita.mezurementz.actors._
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

/**
  * Created by juhofr on 26/03/16.
  */
object Bootstrap extends App {

  // Create akka actor system
  implicit val system = ActorSystem("solita-mezurementz")

  // Initialize actors
  system.actorOf(Props[MeasurementHandler], "measurement-handler")
  system.actorOf(Props[MeasurementEmitter], "measurement-emitter")
  system.actorOf(Props[MeasurementAnalyzer], "measurement-analyzer")
  system.actorOf(Props[AlarmService], "alarm-service")
  val httpService = system.actorOf(Props[MezurementzHttpService], "http-service")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(httpService, interface = "localhost", port = 8080)
}