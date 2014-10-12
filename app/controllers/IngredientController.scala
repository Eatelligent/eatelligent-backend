package controllers

import models.{IngredientForRecipe, IngredientSchema}
import play.api.db.slick.DBAction
import controllers.MyController
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.current.dao._
import models.current.dao.driver.simple._
import play.api.db.slick._
import play.api.mvc._
import play.api.Play.current
import myUtils._

object IngredientController extends MyController {


  def listIngredients = DBAction { implicit request =>
    val json = Json.toJson(ingredients.list)
    Ok(Json.obj("ok" -> true, "ingredients" -> json))
  }

  def saveIngredient = DBAction(BodyParsers.parse.json) { implicit request =>
    val ingredientResult = request.body.validate[IngredientSchema]

    ingredientResult.fold(
      errors => {
        BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
      },
      ingredient => {
        ingredients.insert(IngredientSchema(ingredient.id, ingredient.name, ingredient.image))
        Ok(Json.obj("ok" -> true, "message" -> ("Ingredient '" + ingredient.name + "' saved,")))
      }
    )
  }

  def getIngredient(id: Long) = DBAction { implicit session =>
    val json = Json.toJson(findIngredientById(id))
    Ok(Json.obj("ok" -> true, "ingredient" -> json))
  }

  def getIngredientsForRecipe(recipeId: Long) = DBAction { implicit session =>
    val json = Json.toJson(findIngredientsForRecipe(recipeId).map{
      i => IngredientForRecipe(i.recipeId, i.ingredientId, i.name, i.image, i.amount)
    })
    Ok(Json.obj("ok" -> true, "ingredients" -> json))
  }

}
