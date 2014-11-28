package controllers

import repository.models.{User, Ingredient}

import com.google.inject.Inject
import repository.services.IngredientService
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator

class IngredientController @Inject() (
  ingredientService: IngredientService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {


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


  def listIngredients = SecuredAction.async { implicit request =>
    val ingredients = ingredientService.getAll
    ingredients.map(i => Ok(Json.obj("ok" -> true, "ingredients" -> i)))
  }

//  def saveIngredient = Action.async(BodyParsers.parse.json) { implicit request =>
//    val ingredientResult = request.body.validate[Ingredient]
//
//    ingredientResult.fold(
//      errors => {
//        Future(BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors))))
//      },
//      ingredient => {
//        ingredientService.save(ingredient)
//        Ok(Json.obj("ok" -> true, "message" -> Json.toJson(ingredient)))
//      }
//    )
//  }

  def getIngredient(id: Long) = SecuredAction.async { implicit session =>
    val ingredient =  ingredientService.find(id)
    ingredient.map{
      case Some(i) => Ok(Json.obj("ok" -> true, "ingredient" -> Json.toJson(i)))
      case None => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("No ingredient with id: " + id + " found" +
        ".")))
    }
  }

}
