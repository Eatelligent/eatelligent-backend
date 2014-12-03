package repository.models

import org.joda.time.{LocalDateTime}

case class DateStats (
  date: LocalDateTime,
  number: Int
                       )