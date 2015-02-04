package controllers

import myUtils.JsonFormats
import play.api.mvc.BodyParsers
import repository.daos.IngredientDAO
import repository.models.{User, Ingredient}

import com.google.inject.Inject
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator

import scala.concurrent.Future

class IngredientController @Inject() (
  ingredientDAO: IngredientDAO,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def listIngredients = SecuredAction.async { implicit request =>
    val ingredients = ingredientDAO.getAll
    ingredients.map(i => Ok(Json.obj("ok" -> true, "ingredients" -> i)))
  }

  def getIngredientTags = SecuredAction.async { implicit request =>
    val tags = ingredientDAO.getAllIngredientTags
    tags.map(t => Ok(Json.obj("ok" -> true, "ingredient_tags" -> t)))
  }

  getIngredientTags

  def saveIngredient = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val ingredientResult = request.body.validate[Ingredient]

    ingredientResult.fold(
      errors => {
        Future(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors))))
      },
      ingredient => {
        val savedIngredient = ingredientDAO.save(ingredient)
        savedIngredient.map(i => Ok(Json.obj("ok" -> true, "message" -> Json.toJson(i))))
      }
    )
  }

  def updateIngredient(ingredientId: Long) = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val ingredientPosted = request.body.validate[Ingredient]
    ingredientPosted.fold(
      errors => {
        Future(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors))))
      },
      ingredient => {
        val savedIngredient = ingredientDAO.update(ingredient, ingredientId)
        savedIngredient.map(i => Ok(Json.obj("ok" -> true, "message" -> Json.toJson(i))))
      }
    )
  }

  def getIngredient(id: Long) = SecuredAction.async { implicit session =>
    val ingredient =  ingredientDAO.find(id)
    ingredient.map{
      case Some(i) => Ok(Json.obj("ok" -> true, "ingredient" -> Json.toJson(i)))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("No ingredient with id: " + id + " found" +
        ".")))
    }
  }

}
