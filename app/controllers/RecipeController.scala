package controllers

import java.sql.Date

import controllers.Application._
import models._
import play.api.mvc.Controller
import play.api.db.slick._
import play.api.libs.functional.syntax._
import myUtils._
import play.api.libs.json._
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.Play.current
import models.current.dao._
import models.current.dao.driver.simple._



object RecipeController extends Controller {

//  implicit val jodaDateWrites: Writes[LocalDateTime] = new Writes[LocalDateTime] {
//    def writes(d: LocalDateTime): JsValue = JsString(d.toString())
//  }
//
//  implicit val jodaDateReads: Reads[LocalDateTime] = new Reads[LocalDateTime] {
//    def reads(json:JsValue): LocalDateTime = LocalDateTime.parse(json.as[String])
//  }

//  val recipeForm: Form[Recipe] = Form {
//    mapping(
//      "id" -> optional(number),
//      "name" -> text,
//      "image" -> json,
//      "description" -> text,
//      "language" -> number,
//      "calories" -> number,
//      "procedure" -> text,
//      "created" -> datetime,
//      "modified" -> datetime
//    )(Recipe.apply)(Recipe.unapply)
//  }

  implicit val recipeRead: Reads[Recipe] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[JsValue]] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").read[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "created").read[Date] and
      (JsPath \ "modified").read[Date]
    )(Recipe.apply _)

  implicit val recipeWrites: Writes[Recipe] = (
    (JsPath \ "id").write[Option[Int]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Double] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "created").write[Date] and
      (JsPath \ "modified").write[Date]
    )(unlift(Recipe.unapply))

  def listRecipes = DBAction { implicit request =>
    val json = Json.toJson(recipes.list)
    Ok(Json.obj("ok" -> true, "recipes" -> json))
  }

  def saveRecipe = DBAction(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      recipe => {
        recipes.insert(recipe)
        Ok(Json.obj("ok" -> true, "message" -> ("Recipe '" + recipe.name + "' saved,")))
      }
    )

  }

  def getRecipe = TODO



}
