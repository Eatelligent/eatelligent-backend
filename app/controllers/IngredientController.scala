package controllers

import repository.models.Ingredient

import scala.util.{Success, Failure}
import com.google.inject.Inject
import repository.services.IngredientService
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import play.api.mvc.Controller
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration.Duration

class IngredientController @Inject() (
  ingredientService: IngredientService
                                       ) extends Controller {


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


//  def listIngredients = Action { implicit request =>
//    val json = Json.toJson(ingredientService.getAll)
//    Ok(Json.obj("ok" -> true, "ingredients" -> json))
//  }

  def saveIngredient = Action(BodyParsers.parse.json) { implicit request =>
    val ingredientResult = request.body.validate[Ingredient]

    ingredientResult.fold(
      errors => {
        BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
      },
      ingredient => {
        ingredientService.save(ingredient)
        Ok(Json.obj("ok" -> true, "message" -> Json.toJson(ingredient)))
      }
    )
  }

  def getIngredient(id: Long) = Action.async { implicit session =>
    val ingredient =  ingredientService.find(id)
//    ingredient onComplete  {
//      case Success(i) => Ok(Json.obj("ok" -> true, "ingredient" -> Json.toJson(i)))
//      case Failure(f) => Ok(Json.obj("ok" -> false, "message" -> Json.toJson(f)))
//    }
    ingredient.map(i => Ok(Json.obj("ok" -> true, "ingredient" -> Json.toJson(i))))
  }

//  def getIngredientsForRecipe(recipeId: Long) = Action { implicit session =>
//    val json = Json.toJson(findIngredientsForRecipe(recipeId).map{
//      i => IngredientForRecipe(i.recipeId, i.ingredientId, i.name, i.image, i.amount)
//    })
//    Ok(Json.obj("ok" -> true, "ingredients" -> json))
//  }

}
