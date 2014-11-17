package controllers

import java.util.UUID
import javax.inject.Inject
import play.api.libs.functional.syntax._
import play.api.libs.json._
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
import models.{UserSignUp, User}
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
  extends Silhouette[User, CachedCookieAuthenticator] {

  implicit val signUpRead: Reads[UserSignUp] = (
    (JsPath \ "id").readNullable[UUID] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
    )(UserSignUp.apply _)

  implicit val signUpWrite: Writes[UserSignUp] = (
    (JsPath \ "id").write[Option[UUID]] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "password").write[String]
    )(unlift(UserSignUp.unapply))

  implicit val loginInfoReads: Reads[LoginInfo] = (
    (JsPath \ "providerID").read[String] and
      (JsPath \ "providerKey").read[String]
    )(LoginInfo.apply _)

  implicit val loginInfoWrites: Writes[LoginInfo] = (
    (JsPath \ "providerID").write[String] and
      (JsPath \ "providerKey").write[String]
    )(unlift(LoginInfo.unapply))

  implicit val userRead: Reads[User] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "loginInfo").read[LoginInfo] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "email").readNullable[String] and
      (JsPath \ "image").readNullable[String]
    )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[UUID] and
    (JsPath \ "loginInfo").write[LoginInfo] and
    (JsPath \ "firstName").write[Option[String]] and
    (JsPath \ "lastName").write[Option[String]] and
    (JsPath \ "email").write[Option[String]] and
    (JsPath \ "image").write[Option[String]]
    )(unlift(User.unapply))



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
          userID = UUID.randomUUID(),
          loginInfo = loginInfo,
          firstName = Some(data.firstName),
          lastName = Some(data.lastName),
          email = Some(data.email),
          image = None
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
          userID = UUID.randomUUID(),
          loginInfo = loginInfo,
          firstName = Some(data.firstName),
          lastName = Some(data.lastName),
          email = Some(data.email),
          image = None
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
