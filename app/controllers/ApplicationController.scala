package controllers

import play.api.libs.json.{JsError, Json}
import play.api.{Logger, Play}
import play.api.Play.current
import play.api.mvc._
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.models.User
import scala.concurrent.Future
import javax.inject.Inject
import forms._

class ApplicationController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  def stressTestVerification = Action.async { implicit request =>
    Future.successful(Ok(views.html.loader()))
  }

  def callback = Action.async(BodyParsers.parse.json) { implicit request =>
    Logger.info(request.body.toString())
    Future.successful {
      Ok(Json.obj("ok" -> false, "message" -> "pelle"))
    }
  }

  def index = SecuredAction.async { implicit request =>
    Future.successful(Redirect(routes.UserController.getCurrentUser()))
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signOut = SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))
    Future.successful(env.authenticatorService.discard(Redirect(routes.ApplicationController.index)))
  }

  def adminPanel = UserAwareAction { implicit request =>
    val isDevelopment = Play.configuration.getString("isDevelopment").get.toBoolean
    Ok(views.html.admin(isDevelopment))
  }

  def optionsall(path: String) = UserAwareAction { implicit request =>
    NoContent
  }

  def options = UserAwareAction { implicit request =>
    NoContent
  }

}