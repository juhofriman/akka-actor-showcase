package fi.solita.mezurementz.messages

import java.time.{Instant}

/**
  * Created by juhofr on 26/03/16.
  */
case class Measurement(identifier: String, timestamp: Instant, leftValve: Float, rightValve: Float)
