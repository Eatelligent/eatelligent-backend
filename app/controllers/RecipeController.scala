package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.silhouette.WithRole
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import repository.models._
import repository.services.RecipeService
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


class RecipeController @Inject() (
  val recipeService: RecipeService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

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
      (JsPath \ "createdBy").readNullable[TinyUser]
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
      (JsPath \ "createdBy").write[Option[TinyUser]]
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

  def listRecipes = Action.async { implicit request =>
    val recipes = recipeService.getAll
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }

  def saveRecipe = SecuredAction.async(BodyParsers.parse.json) { implicit request =>
    val recipeResult = request.body.validate[Recipe]
    recipeResult.fold(
      errors => {
        Future {
          BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
        }
      },
      recipe => {
        val newRecipe = recipeService.save(recipe, request.identity)
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

  def getRecipesInTag(q: String) = Action.async { implicit request =>
    val recipes = recipeService.findRecipesInTag(q)
    recipes.map(r => Ok(Json.obj("ok" -> true, "recipes" -> Json.toJson(r))))
  }



}
