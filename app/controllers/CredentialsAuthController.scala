package controllers

import javax.inject.Inject
import com.mohiva.play.silhouette.core.utils.PasswordHasher
import myUtils.JsonFormats
import play.api.i18n.Messages
import play.api.libs.json._
import repository.models.{TokenUser, User}

import scala.concurrent.Future
import play.api.mvc.{BodyParsers, Action}
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core._
import com.mohiva.play.silhouette.core.providers._
import com.mohiva.play.silhouette.core.services.AuthInfoService
import com.mohiva.play.silhouette.core.exceptions.AuthenticationException
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.services.{TokenUserService, MailService, UserService}
import play.api.data._
import play.api.data.Forms._

class CredentialsAuthController @Inject() (
                                            implicit val env: Environment[User, CachedCookieAuthenticator],
                                            val userService: UserService,
                                            val authInfoService: AuthInfoService,
                                            val tokenService: TokenUserService,
                                            val passwordHasher: PasswordHasher,
                                            val mailService: MailService
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

  /**
   * Sends an email to the user with a link to reset the password
   */
  def handleForgotPassword = Action.async(BodyParsers.parse.json) { implicit request =>
    val result = (request.body \ "email").validate[String]
    result.fold (
      errors =>
        Future.successful(
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        ),
      email => {
        userService.find(email).map {
          u => u.isEmpty match {
            case true => NotFound(Json.obj("ok" -> false, "message" -> "No user with that email was found."))
            case false =>
              val token = TokenUser(email, isSignUp = false)
              tokenService.create(token)
              val link = routes.CredentialsAuthController.resetPassword(token.id).absoluteURL()
              mailService.forgotPassword(email, link)
              Ok(Json.obj("ok" -> true, "message" -> Json.toJson("Sent mail with link to reset password " + link)))
          }
        }
      }
    )
  }

  val passwordsForm = Form(tuple(
    "Password" -> nonEmptyText,
    "Repeat password" -> nonEmptyText
  ) verifying(Messages("passwords.not.equal"), passwords => passwords._2 == passwords._1 ))


  /**
   * Confirms the user's link based on the token and shows him a form to reset the password
   */
  def resetPassword (tokenId: String) = Action.async { implicit request =>
    tokenService.retrieve(tokenId).flatMap {
      case Some(token) if !token.isSignUp && !token.isExpired =>
        Future.successful(Ok(views.html.resetPassword(tokenId, passwordsForm)))
      case Some(token) => {
        tokenService.consume(tokenId)
        Future.successful(
          NotFound(Json.obj("ok" -> false, "message" -> "Not found"))
        )
      }
      case None =>
        Future.successful(
          NotFound(Json.obj("ok" -> false, "message" -> "Not found"))
        )
    }
  }

  /**
   * Saves the new password and authenticates the user
   */
  def handleResetPassword (tokenId: String) = Action.async { implicit request =>
    passwordsForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.resetPassword(tokenId, formWithErrors))),
      passwords => {
        tokenService.retrieve(tokenId).flatMap {
          case Some(token) if !token.isSignUp && !token.isExpired => {
            userService.find(token.email).flatMap {
              case Some(user) => {
                val authInfo = passwordHasher.hash(passwords._1)
                authInfoService.save(LoginInfo("credentials", token.email), authInfo)
                for {
                 maybeAuthenticator <- env.authenticatorService.create(user)
                } yield {
                  maybeAuthenticator match {
                    case Some (authenticator) =>
                      env.eventBus.publish (SignUpEvent (user, request, request2lang))
                      env.eventBus.publish (LoginEvent (user, request, request2lang))
                      tokenService.consume (tokenId)
                      env.authenticatorService.send (authenticator, Ok(views.html.resetedPassword()))
                    case None => throw new AuthenticationException ("Couldn't create an authenticator")
                  }
                }
              }
              case None => Future.failed(new AuthenticationException("Couldn't find user"))
            }
          }
          case Some(token) => {
            tokenService.consume(tokenId)
            Future.successful(
              NotFound(Json.obj("ok" -> false, "message" -> "Token expired"))
            )
          }
          case None =>
            Future.successful(
              NotFound(Json.obj("ok" -> false, "message" -> "Token not found."))
            )
        }
      }
    )
  }
}