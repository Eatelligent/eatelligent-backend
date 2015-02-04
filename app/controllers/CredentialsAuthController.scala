package controllers

import javax.inject.Inject
import myUtils.JsonFormats
import play.api.libs.json._
import repository.models.User

import scala.concurrent.Future
import play.api.mvc.{BodyParsers, Action}
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core._
import com.mohiva.play.silhouette.core.providers._
import com.mohiva.play.silhouette.core.services.AuthInfoService
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.services.UserService

class CredentialsAuthController @Inject() (
                                            implicit val env: Environment[User, CachedCookieAuthenticator],
                                            val userService: UserService,
                                            val authInfoService: AuthInfoService
                                           )
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {


  def authenticateJson = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[Credentials].fold (
      errors => Future.successful(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))),
      credentials => (env.providers.get(CredentialsProvider.Credentials) match {
        case Some(p: CredentialsProvider) =>
          p.authenticate(credentials)
        case _ =>
          Future.failed(new AuthenticationException(s"Cannot find credentials provider"))
      }).flatMap { loginInfo =>
        userService.retrieve(loginInfo).flatMap {
          case Some(user) => env.authenticatorService.create(user).map {
            case Some(authenticator) =>
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              env.authenticatorService.send(authenticator, Ok(Json.obj("ok" -> true, "message" -> Json.toJson(user))))
            case None => Unauthorized(Json.obj("ok" -> false, "message" -> "Could not authenticate user."))
          }
          case None => Future.failed(new AuthenticationException("Couldn't find user"))
        }
      }.recoverWith(exceptionHandler)
    )
  }
}