package controllers

import com.google.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import repository.Recipe
import repository.services.RecipeService
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


class RecipeController @Inject() (
  val recipeService: RecipeService
                                   ) extends MyController {

  def listRecipes = Action.async { implicit request =>
    val recipes = recipeService.getAll
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }

  def saveRecipe = Action.async(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
        }
      },
      recipe => {
        val newRecipe = recipeService.save(recipe)
          newRecipe.map(r => Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(r))))
      }
    )
  }

  def getRecipe(id: Long) = Action.async { implicit session =>
    val recipe = recipeService.find(id)
    recipe.map(r => Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(r))))
  }

  def getRecipesByQuery(q: String) = Action.async { implicit session =>
    val recipes = recipeService.find(q)
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }



}
