package fi.solita.mezurementz

import akka.actor.{Props, ActorSystem}
import fi.solita.mezurementz.actors.MeasurementHandler

/**
  * Created by juhofr on 26/03/16.
  */
object Bootstrap extends App {

  // Create akka actor system
  implicit val system = ActorSystem("solita-mezurementz")

  // Initialize actor
  system.actorOf(Props[MeasurementHandler], "measurement-handler")
}