package fi.solita.mezurementz.messages

import java.time.LocalTime

/**
  * Created by juhofr on 26/03/16.
  */
case class AnalyzedMeasurement(measurement: Measurement, payload: String, isAlarm: Boolean)
