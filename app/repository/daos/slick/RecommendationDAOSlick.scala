package repository.daos.slick

import org.joda.time.LocalDateTime
import play.api.libs.json.JsValue
import repository.daos.RecommendationDAO
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import play.api.Play.current
import repository.services.{DummyRec, CBRRec, CFRec, RecommendationMetadata}
import scala.concurrent.Future

class RecommendationDAOSlick extends RecommendationDAO {

  def saveGivenRecommendation(userId: Long, recipeId: Long, from: String, ranking: Int, data: Option[JsValue]) = {
    Future.successful {
      DB withSession { implicit session =>
        slickGivenRecommendations.insert(DBGivenRecommendation(None, userId, recipeId, new LocalDateTime(), from, data))
      }
    }
  }

  def fromJson(json: JsValue): (Long, Double, Double) = {
    (
      (json \ "similarUserId").as[Long],
      (json \ "similarityToUser").as[Double],
      (json \ "similarUserRating").as[Double]
      )
  }

  def getAllGivenRecommendations(limit: Int, offset: Int): Future[Seq[RecommendationMetadata]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickGivenRecommendations.list.map {
          x =>
            x.from match {
              case "cf" =>
                CFRec(x.userId, x.recipeId, x.from, (x.data.get \ "prediction").as[Double])
              case "cbr" =>
                val meta = fromJson(x.data.get)
                CBRRec(x.userId, x.recipeId, x.from, meta._1, meta._2, meta._3)
              case _ =>
                DummyRec(x.userId, x.recipeId, x.from, x.data)
            }
        }
      }
    }
  }

}
