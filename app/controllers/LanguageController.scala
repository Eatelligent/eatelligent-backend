package controllers

import myUtils.JsonFormats
import myUtils.silhouette.WithRole
import org.postgresql.util.PSQLException
import play.api.libs.json._
import play.api.mvc._
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import repository.daos.LanguageDAO
import repository.models.{Language, User}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class LanguageController @Inject() (
  val languageDAO: LanguageDAO,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {


    
  def listLanguages = SecuredAction.async { implicit request =>
      val languages = languageDAO.getAll
      languages.map(ls => {
        if (ls.length > 0) Ok(Json.obj("ok" -> true, "languages" -> ls))
        else NotFound(Json.obj("ok" -> false, "message" -> "No languages found."))
      })
    }

  def getLanguage(id: Long) = SecuredAction.async { implicit request =>
    val l = languageDAO.find(id)
    l.map {
      case Some(lang) => Ok(Json.obj("ok" -> true, "languages" -> lang))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("No language with id: " + id + " found.")))
    }
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
        try {
          val newLanguage = languageDAO.save(language)
          newLanguage.map(l => Created(Json.obj("ok" -> true, "language" -> l)))
        }
        catch {
          case e: PSQLException => Future(Conflict(Json.obj("ok" -> false, "message" -> e.getMessage)))
        }
      }
    )
  }

}
