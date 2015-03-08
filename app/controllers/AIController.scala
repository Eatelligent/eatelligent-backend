package controllers

import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import myUtils.JsonFormats
import play.api.libs.json.Json
import repository.daos.{RecommendationDAO, RecipeDAO}
import repository.models.{RecommendedRecipe, User}
import repository.services.{RecommendationService, RecipeService}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class AIController @Inject() (
                               val recipeService: RecipeService,
                               val recipeDAO: RecipeDAO,
                               val recommendationService: RecommendationService,
                               val recommendationDAO: RecommendationDAO,
                               implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def ai = SecuredAction.async { implicit request =>

    val recs = recommendationService.getRecommendations(request.identity.userID.get)

    val recommendations = recipeDAO.getRecipesFromIds(recs.map(_.recipeId)) map { rs =>
        val recipeMap = rs.map(r => r.id -> r).toMap
        recs.map(
          rec =>
            RecommendedRecipe(rec, recipeMap.get(rec.recipeId).get)
        )
      }
    recommendations.map(r => Ok(Json.obj("ok" -> true, "recommendations" -> Json.toJson(r))))
  }


  def getAllGivenRecommendations(limit: Int, offset: Int) = SecuredAction.async { implicit request =>
    recommendationDAO.getAllGivenRecommendations(limit, offset).map(r =>
      Ok(Json.obj("ok" -> true, "recommendations" -> Json.toJson(r)))
    )
  }

}
