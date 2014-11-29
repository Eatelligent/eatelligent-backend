package repository.daos

import repository.models.Stats

import scala.concurrent.Future

trait StatsDAO {

  def getTotalStats: Future[Stats]

}
