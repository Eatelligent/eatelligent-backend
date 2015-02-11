package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats
import repository.daos.UserViewedRecipeDAO
import repository.models.{UserViewedRecipe, User}

import scala.concurrent.Future

class UserViewedRecipeController @Inject() (
  val userViewedRecipesDAO: UserViewedRecipeDAO,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats{

  def saveUserViewedRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[UserViewedRecipe].fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      json => {
        userViewedRecipesDAO.save(request.identity.userID.get, json.recipeId, json.duration)
          .map(x => Ok(Json.obj("ok" -> true, "userViewedRecipe" -> Json.toJson(x))))
      }
    )
  }

  def listUserViewedRecipes(userId: Option[Long]) = SecuredAction.async { implicit request =>
    userId match {
      case Some(id) =>
        userViewedRecipesDAO.findRecipesUserHasViewed(id)
          .map(x => Ok(Json.obj("ok" -> true, "userViewedRecipe" -> Json.toJson(x))))
      case None =>
        userViewedRecipesDAO.listAll()
          .map(x => Ok(Json.obj("ok" -> true, "userViewedRecipe" -> Json.toJson(x))))
    }
  }

}
