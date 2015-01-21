package controllers

import play.api.Play
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

  /**
   * Handles the index action.
   *
   * @return The result to display.
   */
  def index = SecuredAction.async { implicit request =>
    Future.successful(Ok(views.html.index(request.identity)))
  }

  /**
   * Handles the Sign In action.
   *
   * @return The result to display.
   */
  def signIn = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.ApplicationController.index))
      case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
    }
  }

  /**
   * Handles the Sign Up action.
   *
   * @return The result to display.
   */
  def signUp = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.ApplicationController.index))
      case None => Future.successful(Ok(views.html.signUp(SignUpForm.form)))
    }
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

  def recipeForm = Action { implicit request =>
    Ok(views.html.recipe_form())
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