package repository.daos

import org.joda.time.LocalDateTime
import repository.models.{DateStats, Stats}

import scala.concurrent.Future

trait StatsDAO {

  def getTotalStats: Future[Stats]

  def getUserStats(from: LocalDateTime, to: LocalDateTime): Future[Seq[DateStats]]

  def getRatingStats(from: LocalDateTime, to: LocalDateTime): Future[Seq[DateStats]]

}
