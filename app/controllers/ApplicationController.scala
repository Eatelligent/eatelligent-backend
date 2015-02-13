package controllers

import myUtils.silhouette.WithRole
import play.api.libs.json.Json
import play.api.{Logger, Play}
import play.api.Play.current
import play.api.mvc._
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.models.User
import repository.services.MailService
import scala.concurrent.Future
import javax.inject.Inject

class ApplicationController @Inject() (
                                        val mailService: MailService,
                                        implicit val env: Environment[User, CachedCookieAuthenticator])
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

  def signOut = SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))
    Future.successful(env.authenticatorService.discard(Ok(Json.obj("ok" -> true, "message" -> "logged out."))))
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

  def listRoutes = SecuredAction(WithRole("admin")).async { implicit request =>
    val myroutes = Play.current.routes map (routes => routes.documentation) getOrElse (Nil)
    val out: Seq[String] = myroutes map { r =>
      "%-10s %-50s %s".format(r._1, r._2, r._3)
    }
    Future.successful(Ok(Json.obj("ok" -> true, "routes" -> Json.toJson(out))))
  }

}