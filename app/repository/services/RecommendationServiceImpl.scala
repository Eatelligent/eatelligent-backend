package repository.services

import com.google.inject.Inject
import lenskit.Setup
import play.api.libs.json.{Json, JsValue}
import recommandation.coldstart.UserColdStart
import repository.daos.{RecommendationDAO, RecipeDAO}

class RecommendationServiceImpl @Inject() (
                                          val userColdStart: UserColdStart,
                                          val recipeDAO: RecipeDAO,
                                          val recommendationDAO: RecommendationDAO
                                            ) extends RecommendationService
{


  def getRecommendations(userId: Long): Seq[RecommendationMetadata] = {

    val N = 10
    val CF = 4
    val TAGS = 4
    val RANDOM = 2

    val MIN_TRESHOLD_SCORE_CBR = 0.5
    val NUM_RECIPES_FROM_EACH_USER = 1

    val fromCF: Seq[RecommendationMetadata] = Setup.run(userId, CF)
    val fromTags = userColdStart.findRecipesBasedOnTags(userId, TAGS)
    val fromAgnar: Seq[RecommendationMetadata] = userColdStart.findTheRecipesYouWant(userId, N - fromCF.size -
      fromTags.size - RANDOM, NUM_RECIPES_FROM_EACH_USER, MIN_TRESHOLD_SCORE_CBR)


    val randomNDuTrenger: Seq[RecommendationMetadata] = recipeDAO.getRandomRecipes(N - (fromCF.size + fromAgnar
      .length + fromTags.size))
      .map(x => DummyRec(userId, x, "random", None))

    val res = (util.Random.shuffle(List(fromTags, fromCF, fromAgnar)).flatten ++ randomNDuTrenger).distinct

    res.zipWithIndex.foreach { case (x, index) => saveRecommendation(userId, x, index) }
    res
  }

  def saveRecommendation(userId: Long, rec: RecommendationMetadata, ranking: Int): Unit = {
    rec match {
      case r: CFRec => recommendationDAO.saveGivenRecommendation(userId, r.recipeId, r.from, ranking, Some(r.toJson))
      case r: CBRRec => recommendationDAO.saveGivenRecommendation(userId, r.recipeId, r.from, ranking, Some(r.toJson))
      case r: TagsRec => recommendationDAO.saveGivenRecommendation(userId, r.recipeId, r.from, ranking, Some(r.toJson))
      case _ => recommendationDAO.saveGivenRecommendation(userId, rec.recipeId, rec.from, ranking, None)
    }

  }
}


abstract class RecommendationMetadata {
  def userId: Long
  def recipeId: Long
  def from: String

  override def equals(o: Any) = o match {
    case that: RecommendationMetadata => that.recipeId.equals(this.recipeId)
    case _ => false
  }

}

case class DummyRec(
                   userId: Long,
                   recipeId: Long,
                   from: String,
                   data: Option[JsValue]

                     ) extends RecommendationMetadata

case class TagsRec(
                  userId: Long,
                  recipeId: Long,
                  from: String,
                  score: Double
                  ) extends RecommendationMetadata {
  def toJson: JsValue = {
    Json.obj(
      "score" -> score
    )
  }
}

case class CFRec(
                  userId: Long,
                  recipeId: Long,
                  from: String,
                  prediction: Double
                  ) extends RecommendationMetadata {
  def toJson: JsValue = {
    Json.obj(
      "prediction" -> prediction
    )
  }
}

case class CBRRec(
                 userId: Long,
                 recipeId: Long,
                 from: String,
                 simUserId: Long,
                 simToUser: Double,
                 simUserRating: Double
                   ) extends RecommendationMetadata {
  def toJson: JsValue = {
    Json.obj(
      "similarUserId" -> userId,
      "similarityToUser" -> simToUser,
      "similarUserRating" -> simUserRating
    )
  }


}
