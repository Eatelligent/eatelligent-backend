package repository.daos.slick

import org.joda.time.LocalDateTime
import play.api.libs.json.JsValue
import repository.daos.RecommendationDAO
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import play.api.Play.current
import repository.services._
import scala.concurrent.Future
import scala.slick.jdbc.StaticQuery
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

class RecommendationDAOSlick extends RecommendationDAO {

  def saveGivenRecommendation(userId: Long, recipeId: Long, from: String, ranking: Int, data: Option[JsValue]) = {
    Future.successful {
      DB withSession { implicit session =>
        slickGivenRecommendations.insert(DBGivenRecommendation(None, userId, recipeId, new LocalDateTime(), from,
          ranking, data))
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
              case "tags" =>
                TagsRec(x.userId, x.recipeId, x.from, (x.data.get \ "score").as[Double])
              case _ =>
                DummyRec(x.userId, x.recipeId, x.from, x.data)
            }
        }
      }
    }
  }

  def getAllRecipesToNotShowInRecs(userId: Long): Set[Long] = {
    DB withSession { implicit session =>
      Q.queryNA[Long](
        "SELECT recipe_id " +
        "FROM user_viewed_recipe " +
        "WHERE user_id = " + userId + " AND last_seen > NOW() - INTERVAL '7' DAY " +
        "UNION " +
        "SELECT recipe_id " +
        "FROM user_star_rate_recipe " +
        "WHERE user_id = " + userId + " AND created > NOW() - INTERVAL '14' DAY;"
      ).list.toSet
    }
  }

}
