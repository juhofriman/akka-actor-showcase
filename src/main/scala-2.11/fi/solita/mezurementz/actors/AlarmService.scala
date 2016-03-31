package fi.solita.mezurementz.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import fi.solita.mezurementz.messages.{GetAlarms, Alarm, AnalyzedMeasurement, PleaseAnalyzeThis}

/**
  * Created by juhofr on 26/03/16.
  */
class AlarmService extends Actor with ActorLogging {

  implicit val timeout = Timeout(4, TimeUnit.SECONDS)

  log.info("Hello! I take care of alarms")

  val alarms = scala.collection.mutable.Map[String, scala.collection.mutable.ListBuffer[Alarm]]()

  override def receive: Receive = {
    case alarm: Alarm =>
      alarms.getOrElseUpdate(alarm.measurement.identifier, new scala.collection.mutable.ListBuffer[Alarm]) += alarm

    case command: GetAlarms =>
      command.identifier match {
        case "" => sender() ! collectAllAlarms
        case _ => getAlarmsByIdentifier(command)
      }

    case _ => log.info("Received unrecognized message!")
  }

  def collectAllAlarms: List[Alarm] = {
    alarms.foldLeft(List[Alarm]()) {(acc, entryTuple) =>
      acc ++ entryTuple._2.toList
    }
  }

  def getAlarmsByIdentifier(command: GetAlarms): Unit = {
    alarms.get(command.identifier) match {
        // Note that you can't just directly throw exception to sender()
      case None => sender() ! akka.actor.Status.Failure(new RuntimeException("No such identifer alarmed!"))
      case Some(alarms) => sender() ! alarms.toList
    }
  }
}
