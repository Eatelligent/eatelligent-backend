package controllers

import java.sql.Date

import models.{Recipe, IngredientForRecipe, Ingredient}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, JsValue, JsPath, Reads}
import play.api.mvc.Controller

class MyController extends Controller {

  implicit val ingredientRead: Reads[Ingredient] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue]
    )(Ingredient.apply _)

  implicit val ingredientWrites: Writes[Ingredient] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]]
    )(unlift(Ingredient.unapply))

  implicit val ingredientForRecipeRead: Reads[IngredientForRecipe] = (
    (JsPath \ "ingredientId").read[Long] and
      (JsPath \ "recipeId").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "amount").read[Double]
    )(IngredientForRecipe.apply _)

  implicit val ingredientForRecipeWrites: Writes[IngredientForRecipe] = (
    (JsPath \ "ingredientId").write[Long] and
      (JsPath \ "recipeId").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "amount").write[Double]
    )(unlift(IngredientForRecipe.unapply))


  implicit val recipeRead: Reads[Recipe] = (
    (JsPath \ "id").readNullable[Long] and
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
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Double] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "created").write[Date] and
      (JsPath \ "modified").write[Date]
    )(unlift(Recipe.unapply))

}
