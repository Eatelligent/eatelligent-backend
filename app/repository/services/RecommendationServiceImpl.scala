package repository.services

import com.google.inject.Inject
import lenskit.Setup
import recommandation.coldstart.UserColdStart
import repository.daos.RecipeDAO

class RecommendationServiceImpl @Inject() (
                                          val userColdStart: UserColdStart,
                                          val recipeDAO: RecipeDAO
                                            ) extends RecommendationService
{


  def getRecommendations(userId: Long): Seq[RecommendationMetadata] = {

    val N = 10
    val CF = 5

    val fromCF: Seq[RecommendationMetadata] = Setup.run(userId, CF)

    val fromAgnar: Seq[RecommendationMetadata] = userColdStart.findTheRecipesYouWant(userId, N - fromCF.size, 1, 0.5)

    val randomNDuTrenger: Seq[RecommendationMetadata] = recipeDAO.getRandomRecipes(N - (fromCF.size + fromAgnar.length))
      .map(x => DummyRec(x, "random"))
    (util.Random.shuffle(fromCF ++ fromAgnar) ++ randomNDuTrenger).distinct
  }
}


abstract class RecommendationMetadata {
  def recipeId: Long
  def from: String

  override def equals(o: Any) = o match {
    case that: RecommendationMetadata => that.recipeId.equals(this.recipeId)
    case _ => false
  }

}

case class DummyRec(
                   recipeId: Long,
                   from: String
                     ) extends RecommendationMetadata

case class CFRec(
                  recipeId: Long,
                  from: String,
                  prediction: Double
                  ) extends RecommendationMetadata

case class CBRRec(
                 recipeId: Long,
                 from: String,
                 userId: Long,
                 userSim: Double,
                 simUserRating: Double
                   ) extends RecommendationMetadata

/*


N = 10

1: do the CF part
2: få taki flere oppskrifter
2.1: scan predicted ratings of cbr
2.2 twin with cf list
3 return


  1 cf - 4.5
  2 agnar - 4.5
  3 cf - 4.4
  4 cf - 4.1
  5 cf - 3.7
  6 cf - 2.3
  7
  8
  9
  10










 */