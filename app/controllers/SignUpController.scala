package controllers

import java.util.UUID
import javax.inject.Inject
import myUtils.JsonFormats
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.models.{UserSignUp, User}
import scala.concurrent.Future
import play.api.mvc.{BodyParsers, Action}
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core._
import com.mohiva.play.silhouette.core.providers._
import com.mohiva.play.silhouette.core.utils.PasswordHasher
import com.mohiva.play.silhouette.core.services.{AvatarService, AuthInfoService}
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import models.services.UserService
import forms.SignUpForm

/**
 * The sign up controller.
 *
 * @param env The Silhouette environment.
 * @param userService The user service implementation.
 * @param authInfoService The auth info service implementation.
 * @param avatarService The avatar service implementation.
 * @param passwordHasher The password hasher implementation.
 */
class SignUpController @Inject() (
  implicit val env: Environment[User, CachedCookieAuthenticator],
  val userService: UserService,
  val authInfoService: AuthInfoService,
  val avatarService: AvatarService,
  val passwordHasher: PasswordHasher)
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {


  /**
   * Registers a new user.
   *
   * @return The result to display.
   */
  def signUp = Action.async { implicit request =>
    SignUpForm.form.bindFromRequest.fold (
      form => Future.successful(BadRequest(views.html.signUp(form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.Credentials, data.email)
        val authInfo = passwordHasher.hash(data.password)
        val user = User(
          userID = None,
          loginInfo = loginInfo,
          firstName = Some(data.firstName),
          lastName = Some(data.lastName),
          email = Some(data.email),
          image = None,
          role = Some("user"),
          created = None
        )
        for {
          avatar <- avatarService.retrieveURL(data.email)
          user <- userService.save(user.copy(image = avatar))
          authInfo <- authInfoService.save(loginInfo, authInfo)
          maybeAuthenticator <- env.authenticatorService.create(user)
        } yield {
          maybeAuthenticator match {
            case Some(authenticator) =>
              env.eventBus.publish(SignUpEvent(user, request, request2lang))
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              env.authenticatorService.send(authenticator, Redirect(routes.ApplicationController.index))
            case None => throw new AuthenticationException("Couldn't create an authenticator")
          }
        }
      }
    )
  }

  def signUpJson = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[UserSignUp].fold (
      errors => Future.successful(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.Credentials, data.email)
        val authInfo = passwordHasher.hash(data.password)
        val user = User(
          userID = None,
          loginInfo = loginInfo,
          firstName = Some(data.firstName),
          lastName = Some(data.lastName),
          email = Some(data.email),
          image = None,
          role = Some("user"),
          created = None
        )
        for {
          avatar <- avatarService.retrieveURL(data.email)
          user <- userService.save(user.copy(image = avatar))
          authInfo <- authInfoService.save(loginInfo, authInfo)
          maybeAuthenticator <- env.authenticatorService.create(user)
        } yield {
          maybeAuthenticator match {
            case Some(authenticator) =>
              env.eventBus.publish(SignUpEvent(user, request, request2lang))
              env.eventBus.publish(LoginEvent(user, request, request2lang))
              env.authenticatorService.send(authenticator, Ok(Json.obj("ok" -> true, "message" -> Json.toJson(user))))
            case None => throw new AuthenticationException("Couldn't create an authenticator")
          }
        }
      }
    )
  }
}
