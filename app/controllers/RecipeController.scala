package controllers

import models._
import play.api.db.slick._
import play.api.libs.json._
import play.api.mvc._
import play.api.Play.current
import models.current.dao._
import models.current.dao.driver.simple._



object RecipeController extends MyController {

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

  def getRecipe(id: Long) = DBAction { implicit session =>
    val recipe = findRecipeById(id)
    val ingredients = findIngredientsForRecipe(id)
    Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(recipe), "ingredients" -> Json.toJson(ingredients)))
  }



}
