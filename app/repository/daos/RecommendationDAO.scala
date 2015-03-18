package repository.daos

import play.api.libs.json.JsValue
import repository.services.RecommendationMetadata

import scala.concurrent.Future

trait RecommendationDAO {

  def saveGivenRecommendation(userId: Long, recipeId: Long, from: String, ranking: Int, data: Option[JsValue])

  def getAllGivenRecommendations(limit: Int, offset: Int): Future[Seq[RecommendationMetadata]]

  def getAllRecipesToNotShowInRecs(userId: Long): Set[Long]

}
