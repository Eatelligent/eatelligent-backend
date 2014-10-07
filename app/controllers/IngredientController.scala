package controllers

import models.Ingredient
import play.api.db.slick.DBAction
import play.api.mvc.Controller
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.current.dao._
import models.current.dao.driver.simple._
import play.api.db.slick._
import play.api.mvc._
import play.api.Play.current

object IngredientController extends Controller {

  implicit val ingredientRead: Reads[Ingredient] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue]
    )(Ingredient.apply _)
  
  implicit val recipeWrites: Writes[Ingredient] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]]
    )(unlift(Ingredient.unapply))
  
  def listIngredients = DBAction { implicit request =>
    val json = Json.toJson(ingredients.list)
    Ok(Json.obj("ok" -> true, "ingredients" -> json))
  }

  def saveIngredient = DBAction(BodyParsers.parse.json) { implicit request =>
    val ingredientResult = request.body.validate[Ingredient]
    ingredientResult.fold(
      errors => {
        BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
      },
      ingredient => {
        ingredients.insert(ingredient)
        Ok(Json.obj("ok" -> true, "message" -> ("Ingredient '" + ingredient.name + "' saved,")))
      }
    )
  }

  def getIngredient(id: Long) = DBAction { implicit session =>
    val json = Json.toJson(findById(id))
    Ok(Json.obj("ok" -> true, "ingredient" -> json))
  }

}
