package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.iteratee.Enumerator
import play.api.libs.json._
import play.api.mvc._
import play.api.db.slick._
import play.api.Play.current
import myUtils._
import models._
import play.api.libs.functional.syntax._

import models.current.dao._
import models.current.dao.driver.simple._

object Application extends Controller {


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

  val languageForm: Form[Language] = Form {
      mapping(
        "id" -> optional(number),
        "locale" -> nonEmptyText,
        "name" -> nonEmptyText
      )(Language.apply)(Language.unapply)
  }

  def index = DBAction { implicit request =>
    Ok(views.html.index(languages.list))
  }

  def insert = DBAction { implicit rs =>
//    val language = languageForm.bindFromRequest.get
//    languages.insert(language)
    Redirect(routes.Application.index)
  }

  def lasser = Action {
    Result(
      header = ResponseHeader(200, Map(CONTENT_TYPE -> "text/json")),
      body = Enumerator("Hello lasser!".getBytes)
    )
  }

  def users = TODO


  def pelle = TODO

}