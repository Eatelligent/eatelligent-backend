package controllers

import java.sql.Date

import org.joda.time.DateTime
import repository._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, JsValue, JsPath, Reads}
import play.api.mvc.Controller
import repository.TagSchema

case class Unit(
                  id: Option[Long],
                  name: String
                 )

class MyController extends Controller {

  implicit val tinyUserRead: Reads[TinyUser] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String]
    )(TinyUser.apply _)

  implicit val tinyUserWrite: Writes[TinyUser] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String]
    )(unlift(TinyUser.unapply))

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

  implicit val tagRead: Reads[TagSchema] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String]
    )(TagSchema.apply _)

  implicit val tagWrite: Writes[TagSchema] =(
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String]
    )(unlift(TagSchema.unapply))

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
      (JsPath \ "image").read[Option[String]] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").read[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "modified").readNullable[DateTime] and
      (JsPath \ "ingredients").read[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").read[Seq[TagSchema]] and
      (JsPath \ "createdBy").read[TinyUser]
    )(Recipe.apply _)

  implicit val recipeWrites: Writes[Recipe] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Double] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "created").write[Option[DateTime]] and
      (JsPath \ "modified").write[Option[DateTime]] and
      (JsPath \ "ingredients").write[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").write[Seq[TagSchema]] and
      (JsPath \ "createdBy").write[TinyUser]
    )(unlift(Recipe.unapply))

  implicit val recipeSchemaRead: Reads[RecipeSchema] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[String]] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").read[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "created").read[DateTime] and
      (JsPath \ "modified").read[DateTime] and
      (JsPath \ "createdById").read[Long]
    )(RecipeSchema.apply _)

  implicit val recipeSchemaWrites: Writes[RecipeSchema] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Double] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "created").write[DateTime] and
      (JsPath \ "modified").write[DateTime] and
      (JsPath \ "createdById").write[Long]
    )(unlift(RecipeSchema.unapply))

  implicit val tinyRecipeRead: Reads[TinyRecipe] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[String]]
    )(TinyRecipe.apply _)

  implicit val tinyRecipeWrites: Writes[TinyRecipe] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]]
    )(unlift(TinyRecipe.unapply))



}
