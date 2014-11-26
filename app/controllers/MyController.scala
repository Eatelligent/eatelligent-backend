package controllers

import org.joda.time.DateTime
import repository._
import play.api.libs.functional.syntax._
import play.api.libs.json.{Writes, JsValue, JsPath, Reads}
import play.api.mvc.Controller

class MyController extends Controller {

  implicit val recipeImageReads: Writes[RecipeImage] = (
    (JsPath \ "recipeId").write[Long] and
      (JsPath \ "url").write[String]
    )(unlift(RecipeImage.unapply))


  implicit val tinyUserRead: Reads[TinyUser] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String]
    )(TinyUser.apply _)

  implicit val tinyUserWrite: Writes[TinyUser] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "firstName").write[Option[String]] and
      (JsPath \ "lastName").write[Option[String]]
    )(unlift(TinyUser.unapply))

  implicit val ingredientSchemaRead: Reads[Ingredient] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue]
    )(Ingredient.apply _)

  implicit val ingredientSchemaWrites: Writes[Ingredient] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]]
    )(unlift(Ingredient.unapply))

  implicit val tagRead: Reads[RecipeTag] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String]
    )(RecipeTag.apply _)

  implicit val tagWrite: Writes[RecipeTag] =(
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String]
    )(unlift(RecipeTag.unapply))

  implicit val ingredientForRecipeRead: Reads[IngredientForRecipe] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "amount").read[Double]
//    (JsPath \ "unit").readNullable[Unit]
    )(IngredientForRecipe.apply _)

  implicit val ingredientForRecipeWrites: Writes[IngredientForRecipe] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "amount").write[Double]
//        (JsPath \ "unit").writeNullable[Unit]
    )(unlift(IngredientForRecipe.unapply))

//  implicit val unitRead: Reads[Unit] = (
//    (JsPath \ "id").readNullable[Long] and
//      (JsPath \ "name").read[String]
//    )(Unit.apply _)
//
//  implicit val unitWrites: Writes[Unit] = (
//    (JsPath \ "id").writeNullable[Long] and
//      (JsPath \ "name").write[String]
//    )(unlift(Unit.unapply))


  implicit val recipeRead: Reads[Recipe] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").readNullable[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "spicy").read[Int] and
      (JsPath \ "created").readNullable[DateTime] and
      (JsPath \ "modified").readNullable[DateTime] and
      (JsPath \ "ingredients").read[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").read[Seq[String]] and
      (JsPath \ "createdBy").read[TinyUser]
    )(Recipe.apply _)

  implicit val recipeWrites: Writes[Recipe] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Option[Double]] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "spicy").write[Int] and
      (JsPath \ "created").write[Option[DateTime]] and
      (JsPath \ "modified").write[Option[DateTime]] and
      (JsPath \ "ingredients").write[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").write[Seq[String]] and
      (JsPath \ "createdBy").write[TinyUser]
    )(unlift(Recipe.unapply))

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
