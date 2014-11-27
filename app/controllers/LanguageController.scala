package controllers

import myUtils.silhouette.WithRole
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.models.{Language, User}
import repository.services.LanguageService
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class LanguageController @Inject() (
  val languageService: LanguageService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

    implicit val languageWrites: Writes[Language] = (
      (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "locale").write[String] and
      (JsPath \ "name").write[String]
    )(unlift(Language.unapply))
  
    implicit val languageRead: Reads[Language] = (
      (JsPath \ "id").readNullable[Long] and
        (JsPath \ "locale").read[String] and
        (JsPath \ "name").read[String]
      )(Language.apply _)
    
  def listLanguages = SecuredAction.async { implicit request =>
      val languages = languageService.getAll
      languages.map(ls => Ok(Json.obj("ok" -> true, "languages" -> ls)))
    }

  def getLanguage(id: Long) = SecuredAction.async { implicit request =>
    val l = languageService.find(id)
    l.map(l => Ok(Json.obj("ok" -> true, "language" -> l)))
  }
  
  def saveLanguage = SecuredAction(WithRole("admin")).async(BodyParsers.parse.json) { implicit request =>
    val languageResult = request.body.validate[Language]
    languageResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      language => {
        val newLanguage = languageService.save(language)
        newLanguage.map(l => Ok(Json.obj("ok" -> true, "language" -> l)))
      }
    )
  }

}
