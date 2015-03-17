package repository.daos.slick

import repository.daos.ColdStartDAO
import repository.models.{ColdStart, UserColdStart}
import org.joda.time.LocalDateTime
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._

import scala.concurrent.Future

class ColdStartDAOSlick extends ColdStartDAO {
  import play.api.Play.current

  def saveColdStartResponse(userId: Long, userColdStart: UserColdStart): Future[UserColdStart] = {
    Future.successful {
      val newInstance = UserColdStart(
        userId = Some(userId),
        coldStartId = userColdStart.coldStartId,
        answer = userColdStart.answer,
        answerTime = Some(new LocalDateTime)
      )
      DB withSession { implicit session =>
        slickUserColdStarts.filter(x => x.userId === userId && x.coldStartId === userColdStart.coldStartId)
          .firstOption match {
            case Some(found) =>
              slickUserColdStarts.filter(x => x.userId === userId && x.coldStartId === userColdStart.coldStartId).delete
            case None =>
        }
          slickUserColdStarts.insert(DBUserColdStart(
          userId = newInstance.userId.get,
          coldStartId = newInstance.coldStartId,
          answer = newInstance.answer,
          answerTime = newInstance.answerTime.get
         ))
      }
      newInstance
    }
  }

  def getColdStartsForUser(userId: Long): Seq[UserColdStart] = {
    DB withSession { implicit session =>
      slickUserColdStarts.filter(_.userId === userId)
        .list
        .map(x => UserColdStart(Some(userId), x.coldStartId, x.answer, Some(x.answerTime)))
    }
  }

  def getColdStarts: Future[Seq[ColdStart]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickColdStarts
          .list
          .map(c =>
          ColdStart(
            id = c.id,
            image = c.image,
            identifier = c.identifier,
            description = c.description
        ))
      }
    }
  }

}
