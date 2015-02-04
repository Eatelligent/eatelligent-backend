package controllers

import javax.inject.Inject
import myUtils.JsonFormats
import myUtils.silhouette.WithRole
import play.api.libs.json._
import repository.models.{UserUpdate, User}
import scala.concurrent.Future
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core._
import com.mohiva.play.silhouette.core.utils.PasswordHasher
import com.mohiva.play.silhouette.core.services.{AvatarService, AuthInfoService}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.services.UserService

class UserController @Inject() (
                                   implicit val env: Environment[User, CachedCookieAuthenticator],
                                   val userService: UserService,
                                   val authInfoService: AuthInfoService,
                                   val avatarService: AvatarService,
                                   val passwordHasher: PasswordHasher)
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def getUser(userId: Long) = SecuredAction(WithRole("admin")).async { implicit request =>
    userService.findUserByUID(userId) map {
      case Some(user) => Ok(Json.obj("ok" -> true, "user" -> user))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("Could not find any user with id: " +
        userId)))
    }
  }

  def listUsers(offset: Integer, limit: Integer) = SecuredAction(WithRole("admin")).async { implicit
                                                                                                      request =>
    userService.getAll(offset, limit) map (us => Ok(Json.obj("ok" -> true, "users" -> us)))
  }

  def getCurrentUser = SecuredAction.async { implicit request =>
    userService.findUserByUID(request.identity.userID.get) map {
      case Some(user) => Ok(Json.obj("ok" -> true, "user" -> user))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("Could not find current user")))
    }
  }

  def updateUser = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val userResult = request.body.validate[UserUpdate]
    userResult.fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      user => {
        val newUser = userService.update(user, request.identity.userID.get)
        newUser.map(u => Ok(Json.obj("ok" -> true, "user" -> Json.toJson(u))))
      }
    )
  }



}
