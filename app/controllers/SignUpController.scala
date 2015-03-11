package controllers

import javax.inject.Inject
import myUtils.JsonFormats
import play.api.libs.json._
import play.api.mvc.Results._
import repository.exceptions.DuplicateException
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
import repository.services.UserService

class SignUpController @Inject() (
  implicit val env: Environment[User, CachedCookieAuthenticator],
  val userService: UserService,
  val authInfoService: AuthInfoService,
  val avatarService: AvatarService,
  val passwordHasher: PasswordHasher)
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {


  def signUpJson = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[UserSignUp].fold (
      errors => Future.successful(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.Credentials, data.email)
        val authInfo = passwordHasher.hash(data.password)
        val user = User(
          userID = None,
          loginInfo = loginInfo,
          firstName = data.firstName,
          lastName = data.lastName,
          email = Some(data.email),
          image = None,
          role = Some("user"),
          created = None,
          recipeLanguage = None,
          appLanguage = None,
          city = None,
          country = None,
          sex = None,
          yearBorn = None,
          enrolled = None,
          metricSystem = None
        )
        try {
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
        catch {
          case e: DuplicateException => Future(Conflict(Json.obj("ok" -> false, "message" -> Json.toJson(e
            .getMessage))))
        }
      }
    )
  }
}
