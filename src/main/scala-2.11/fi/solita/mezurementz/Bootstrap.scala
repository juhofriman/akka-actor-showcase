package fi.solita.mezurementz

import akka.actor.{Props, ActorSystem}

/**
  * Created by juhofr on 26/03/16.
  */
object Bootstrap extends App {

  // Create akka actor system
  implicit val system = ActorSystem("solita-mezurementz")

}