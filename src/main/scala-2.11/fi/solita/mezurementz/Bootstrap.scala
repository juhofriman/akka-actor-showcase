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


  system.actorOf(Props(new MeasurementEmitter("mez-1", 1999.millisecond, 1261.millisecond)), "measurement-emitter-1")
  system.actorOf(Props(new MeasurementEmitter("mez-2", 2155.millisecond, 112.millisecond)), "measurement-emitter-2")
  system.actorOf(Props(new MeasurementEmitter("mez-3", 251.millisecond, 3982.millisecond)), "measurement-emitter-3")
  system.actorOf(Props(new MeasurementEmitter("mez-4", 636.millisecond, 515.millisecond)), "measurement-emitter-4")
  system.actorOf(Props(new MeasurementEmitter("mez-5", 353.millisecond, 664.millisecond)), "measurement-emitter-5")
  system.actorOf(Props(new MeasurementEmitter("mez-6", 351.millisecond, 2415.millisecond)), "measurement-emitter-6")
  system.actorOf(Props(new MeasurementEmitter("mez-7", 666.millisecond, 3646.millisecond)), "measurement-emitter-7")
  system.actorOf(Props(new MeasurementEmitter("mez-8", 7547.millisecond, 4664.millisecond)), "measurement-emitter-8")
  system.actorOf(Props(new MeasurementEmitter("mez-9", 636.millisecond, 1515.millisecond)), "measurement-emitter-9")
  system.actorOf(Props(new MeasurementEmitter("mez-10", 653.millisecond, 5955.millisecond)), "measurement-emitter-10")
  system.actorOf(Props(new MeasurementEmitter("mez-11", 777.millisecond, 2151.millisecond)), "measurement-emitter-11")
  system.actorOf(Props(new MeasurementEmitter("mez-12", 525.millisecond, 1000.millisecond)), "measurement-emitter-12")
  system.actorOf(Props(new MeasurementEmitter("mez-13", 775.millisecond, 2525.millisecond)), "measurement-emitter-13")
  system.actorOf(Props(new MeasurementEmitter("mez-14", 888.second, 10000.millisecond)), "measurement-emitter-14")


  system.actorOf(Props[MeasurementAnalyzer], "measurement-analyzer")
  system.actorOf(Props[AlarmService], "alarm-service")
  val httpService = system.actorOf(Props[MezurementzHttpService], "http-service")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(httpService, interface = "localhost", port = 8080)
}