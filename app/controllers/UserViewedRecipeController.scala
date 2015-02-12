package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats
import repository.daos.UserViewedRecipeDAO
import repository.models.{UserViewedRecipePost, UserViewedRecipe, User}

import scala.concurrent.Future

class UserViewedRecipeController @Inject() (
  val userViewedRecipesDAO: UserViewedRecipeDAO,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats{

  def saveUserViewedRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[UserViewedRecipePost].fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      json => {
        userViewedRecipesDAO.save(request.identity.userID.get, json.recipeId, json.duration)
          .map(x => Ok(Json.obj("ok" -> true, "viewedRecipe" -> Json.toJson(x))))
      }
    )
  }

  def listUserViewedRecipes = SecuredAction.async { implicit request =>
    userViewedRecipesDAO.findRecipesUserHasViewed(request.identity.userID.get)
      .map(x => Ok(Json.obj("ok" -> true, "viewedRecipes" -> Json.toJson(x))))
  }

}
