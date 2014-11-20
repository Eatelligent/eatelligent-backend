package controllers

import com.google.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import repository.Recipe
import repository.services.RecipeService
import play.api.libs.concurrent.Execution.Implicits._


class RecipeController @Inject() (
  val recipeService: RecipeService
                                   ) extends MyController {

  def listRecipes = Action.async { implicit request =>
    val recipes = recipeService.getAll
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }

  def saveRecipe = Action(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      recipe => {
        recipeService.save(recipe)
        Ok(Json.obj("ok" -> true, "message" -> recipe))
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
