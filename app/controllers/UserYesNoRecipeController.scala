package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import play.api.Logger
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats
import repository.daos.{UserTagRelationDAO, UserYesNoRecipeDAO}
import repository.models.{UserYesNoRecipe, User}

import scala.concurrent.Future

class UserYesNoRecipeController @Inject() (
                                             val userYesNoRecipeDAO: UserYesNoRecipeDAO,
                                             val userTagRelationDAO: UserTagRelationDAO,
                                             implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats{

  def saveUserYesNoRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>




    request.body.validate[UserYesNoRecipe].fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      json => {
        val WEIGHT = 0.3
        Logger.info("Yes / no: " + json.rating)
        userTagRelationDAO.updateTagValuesForUser(json.recipeId, request.identity.userID.get, WEIGHT * json.rating)
        userYesNoRecipeDAO.save(request.identity.userID.get, json.recipeId, json.rating)
          .map(x => Ok(Json.obj("ok" -> true, "result" -> Json.toJson(x))))
      }
    )
  }

}
