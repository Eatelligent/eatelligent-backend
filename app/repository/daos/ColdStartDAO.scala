package repository.daos

import repository.models.{ColdStart, UserColdStart}

import scala.concurrent.Future

trait ColdStartDAO {

  def saveColdStartResponse(userId: Long, userColdStart: UserColdStart): Future[UserColdStart]

  def getColdStarts: Future[Seq[ColdStart]]

}
