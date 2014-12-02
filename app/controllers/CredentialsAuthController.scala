package controllers

import java.util.UUID
import javax.inject.Inject
import org.joda.time.DateTime
import play.api.Logger
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
import models.services.UserService
import forms.SignInForm
import play.api.libs.functional.syntax._

/**
 * The credentials auth controller.
 *
 * @param env The Silhouette environment.
 */
class CredentialsAuthController @Inject() (
                                            implicit val env: Environment[User, CachedCookieAuthenticator],
                                            val userService: UserService,
                                            val authInfoService: AuthInfoService
                                           )
  extends Silhouette[User, CachedCookieAuthenticator] {

  implicit val credentialsReads: Reads[Credentials] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
    )(Credentials.apply _)

  implicit val loginInfoWrites: Writes[LoginInfo] = (
    (JsPath \ "providerID").write[String] and
      (JsPath \ "providerKey").write[String]
    )(unlift(LoginInfo.unapply))

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "loginInfo").write[LoginInfo] and
      (JsPath \ "firstName").write[Option[String]] and
      (JsPath \ "lastName").write[Option[String]] and
      (JsPath \ "email").write[Option[String]] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "role").write[Option[String]] and
      (JsPath \ "created").write[Option[DateTime]]
    )(unlift(User.unapply))

  /**
   * Authenticates a user against the credentials provider.
   *
   * @return The result to display.
   */
  def authenticate = Action.async { implicit request =>
    SignInForm.form.bindFromRequest.fold (
      form => Future.successful(BadRequest(views.html.signIn(form))),
      credentials => (env.providers.get(CredentialsProvider.Credentials) match {
        case Some(p: CredentialsProvider) => p.authenticate(credentials)
        case _ => Future.failed(new AuthenticationException(s"Cannot find credentials provider"))
      }).flatMap { loginInfo =>
        userService.retrieve(loginInfo).flatMap {
          case Some(user) => env.authenticatorService.create(user).map {
            case Some(authenticator) =>
              Logger.info("Authenticator: " + authenticator)
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              Logger.info("Redirecting to index")
              Logger.info("Identity: " + request.toString())
              env.authenticatorService.send(authenticator, Redirect(routes.ApplicationController.index))
            case None => throw new AuthenticationException("Couldn't create an authenticator")
          }
          case None => Future.failed(new AuthenticationException("Couldn't find user"))
        }
      }.recoverWith(exceptionHandler)
    )
  }

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