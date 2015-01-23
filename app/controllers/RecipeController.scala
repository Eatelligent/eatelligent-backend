package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import myUtils.silhouette.{IsAuthor, WithRole}
import play.api.libs.json._
import play.api.mvc._
import repository.models._
import repository.services.RecipeService
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class RecipeController @Inject() (
  val recipeService: RecipeService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def listRecipes(q: String, offset: Integer , limit: Integer, published: Boolean, deleted: Boolean, language: Long,
                  tag: Option[String]) =
    SecuredAction(WithRole("admin")).async { implicit request =>
    val recipes = recipeService.find(q, offset, limit, published, deleted, language, tag)
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }

  def saveRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      recipe => {
        val newRecipe = recipeService.save(recipe, request.identity)
        newRecipe.map(r => Created(Json.obj("ok" -> true, "recipe" -> Json.toJson(r))))
      }
    )
  }

  def updateRecipe(recipeId: Long) = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      recipe => {
        val newRecipe = recipeService.update(recipe.copy(id = Some(recipeId)), request.identity)
        newRecipe.map(r => Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(r))))
      }
    )
  }

  def getRecipe(id: Long) = SecuredAction.async { implicit request =>
    val recipe = recipeService.find(id, request.identity.userID.get)
    recipe.map {
      case Some(r) => Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(r)))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("No recipe with id: " + id + " found.")))
    }
  }

  def getRecipesInTag(q: String) = SecuredAction.async { implicit request =>
    val recipes = recipeService.findRecipesInTag(q)
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }

  def deleteRecipe(id: Long) = SecuredAction(
    IsAuthor(
      Await.result(recipeService.find(id, -1), Duration(1, "seconds")) match {
        case Some(r) => r.createdBy.get.id
        case None => -1
      }
    )).async { implicit request =>
    val recipe = recipeService.deleteRecipe(id, request.identity.userID.get)
    recipe.map(r => Ok(Json.obj("ok" -> true, "recipe" -> Json.toJson(r))))
  }


}
