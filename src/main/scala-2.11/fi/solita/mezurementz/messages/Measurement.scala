package fi.solita.mezurementz.messages

import java.time.LocalTime

/**
  * Created by juhofr on 26/03/16.
  */
case class Measurement(identifier: String, timestamp: LocalTime, leftValve: Float, rightValve: Float)
