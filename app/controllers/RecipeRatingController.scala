package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import play.api.libs.json._
import play.api.mvc._
import repository.daos.{UserTagRelationDAO, RatingDAO}
import repository.models._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


class RecipeRatingController @Inject() (
                                   val ratingDAO: RatingDAO,
                                       val userTagRelationDAO: UserTagRelationDAO,
                                   implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def listRatingsForUser = SecuredAction.async { implicit request =>
    val ratings = ratingDAO.findStarRatingsForUser(request.identity.userID.get)
    ratings.map(r => Ok(Json.obj("ok" -> true, "ratings" -> r)))
  }

  def listRatingsForRecipe(recipeId: Long) = SecuredAction.async { implicit request =>
    val ratings = ratingDAO.findStarRatingsForRecipe(recipeId)
    ratings.map(r => Ok(Json.obj("ok" -> true, "ratings" -> r)))
  }

  def saveStarRatingRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val ratingResult = request.body.validate[UserStarRateRecipe]
    ratingResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      rating => {
        val userId = request.identity.userID.get
        val delta = rating.stars match {
          case 1.0 => -1.0
          case 2.0 => -0.5
          case 4.0 => 0.5
          case 5.0 => 1.0
          case _ => 0.0
        }
        userTagRelationDAO.updateTagValuesForUser(rating.recipeId, userId, delta)
        val newRating = ratingDAO.saveUserStarRateRecipe(rating.copy(userId = request.identity.userID))
        newRating.map(r => Created(Json.obj("ok" -> true, "rating" -> r)))
      }
    )
  }

}
