package controllers

import java.sql.Date

import models._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, JsValue, JsPath, Reads}
import play.api.mvc.Controller




//case class Ingredient(
//                       id: Option[Long],
//                       name: String,
//                       image: Option[JsValue]
//                       )
//
//case class IngredientInRecipe(
//                                ingredientId: Long,
//                                recipeId: Long,
//                                name: String,
//                                image: Option[JsValue],
//                                amount: Double,
//                                unit: Option[Unit]
//                                )
case class Unit(
                  id: Option[Long],
                  name: String
                 )

class MyController extends Controller {

  implicit val ingredientSchemaRead: Reads[IngredientSchema] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue]
    )(IngredientSchema.apply _)

  implicit val ingredientSchemaWrites: Writes[IngredientSchema] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]]
    )(unlift(IngredientSchema.unapply))



  implicit val ingredientForRecipeRead: Reads[IngredientForRecipe] = (
    (JsPath \ "ingredientId").readNullable[Long] and
      (JsPath \ "recipeId").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "amount").read[Double]
//    (JsPath \ "unit").readNullable[Unit]
    )(IngredientForRecipe.apply _)

  implicit val ingredientForRecipeWrites: Writes[IngredientForRecipe] = (
    (JsPath \ "ingredientId").writeNullable[Long] and
      (JsPath \ "recipeId").writeNullable[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "amount").write[Double]
//        (JsPath \ "unit").writeNullable[Unit]
    )(unlift(IngredientForRecipe.unapply))

  implicit val unitRead: Reads[Unit] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String]
    )(Unit.apply _)

  implicit val unitWrites: Writes[Unit] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "name").write[String]
    )(unlift(Unit.unapply))


  implicit val recipeRead: Reads[Recipe] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[JsValue]] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").read[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "created").read[Date] and
      (JsPath \ "modified").read[Date] and
      (JsPath \ "ingredients").read[Seq[IngredientForRecipe]]
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
      (JsPath \ "modified").write[Date] and
      (JsPath \ "ingredients").write[Seq[IngredientForRecipe]]
    )(unlift(Recipe.unapply))

  implicit val recipeSchemaRead: Reads[RecipeSchema] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[JsValue]] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").read[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "created").read[Date] and
      (JsPath \ "modified").read[Date]
    )(RecipeSchema.apply _)

  implicit val recipeSchemaWrites: Writes[RecipeSchema] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Double] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "created").write[Date] and
      (JsPath \ "modified").write[Date]
    )(unlift(RecipeSchema.unapply))

  implicit val tinyRecipeRead: Reads[TinyRecipe] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[JsValue]]
    )(TinyRecipe.apply _)

  implicit val tinyRecipeWrites: Writes[TinyRecipe] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]]
    )(unlift(TinyRecipe.unapply))
}
