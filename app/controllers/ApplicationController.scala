package controllers

import models.User
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import myUtils._
import repository._
import play.api.libs.functional.syntax._
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import forms._
import repository.current.dao._
import repository.current.dao.driver.simple._

class ApplicationController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {


  implicit val languageWrites: Writes[Language] = (
    (JsPath \ "id").write[Option[Int]] and
    (JsPath \ "locale").write[String] and
    (JsPath \ "name").write[String]
  )(unlift(Language.unapply))

  def listLanguages = DBAction { implicit request =>
    val json = Json.toJson(languages.list)
    Ok(Json.obj("ok" -> true, "languages" -> json))
  }

  implicit val languageRead: Reads[Language] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "locale").read[String] and
      (JsPath \ "name").read[String]
    )(Language.apply _)

  def saveLanguage = DBAction(BodyParsers.parse.json) { implicit request =>
    val languageResult = request.body.validate[Language]
    languageResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      language => {
        languages.insert(language)
        Ok(Json.obj("status" -> "OK", "message" -> ("Place '" + language.name + "' saved,")))
      }
    )
  }

//  val languageForm: Form[Language] = Form {
//      mapping(
//        "id" -> optional(number),
//        "locale" -> nonEmptyText,
//        "name" -> nonEmptyText
//      )(Language.apply)(Language.unapply)
//  }

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

  def recipeForm = DBAction { implicit request =>
    Ok(views.html.recipe_form())
  }

  def adminPanel = Action { implicit request => 
    Ok(views.html.admin())
  }

}