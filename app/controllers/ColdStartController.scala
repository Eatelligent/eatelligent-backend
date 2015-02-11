package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats
import repository.daos.ColdStartDAO
import repository.models.{UserColdStart, User}

import scala.concurrent.Future

class ColdStartController @Inject() (
                                             val coldStartDAO: ColdStartDAO,
                                             implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats{

  def saveColdStartResponse = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[UserColdStart].fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      data => {
        coldStartDAO.saveColdStartResponse(request.identity.userID.get, data)
          .map(x => Ok(Json.obj("ok" -> true, "answer" -> Json.toJson(x))))
      }
    )
  }

  def listColdStarts = SecuredAction.async { implicit request =>
        coldStartDAO.getColdStarts
          .map(x => Ok(Json.obj("ok" -> true, "choices" -> Json.toJson(x))))
  }

}
