package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import play.api.mvc.BodyParsers
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats
import myUtils.ColdStartConstants
import myUtils.IngredientTagConstants
import repository.daos.{UserTagRelationDAO, ColdStartDAO}
import repository.models.{UserColdStart, User}

import scala.concurrent.Future

class ColdStartController @Inject() (
                                             val coldStartDAO: ColdStartDAO,
                                             val userTagRelationDAO: UserTagRelationDAO,
                                             implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats{





  val coldStartMap: Map[Long, Map[Long, Double]] =
    Map(
      ColdStartConstants.SPICY -> Map(
        IngredientTagConstants.Spice -> 0.5,
        IngredientTagConstants.Spicy -> 1.0,
        IngredientTagConstants.Salty -> 0.2
      ),
      ColdStartConstants.SKILLS -> Map(
        IngredientTagConstants.Composed -> -1.0
      ),
      ColdStartConstants.FISH -> Map(
        IngredientTagConstants.Fish -> 1.0,
        IngredientTagConstants.Seafood -> 0.8,
        IngredientTagConstants.AnimalProduct -> 0.1
      ),
      ColdStartConstants.CHICKEN -> Map(
        IngredientTagConstants.Chicken -> 1.0,
        IngredientTagConstants.AnimalProduct -> 0.2
      ),
      ColdStartConstants.MEAT -> Map(
        IngredientTagConstants.Meat -> 1.0,
        IngredientTagConstants.Pork -> 0.8,
        IngredientTagConstants.Beef -> 0.8,
        IngredientTagConstants.AnimalProduct -> 0.3
      )
    )

  def saveColdStartResponse = SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    request.body.validate[UserColdStart].fold(
      errors => {
        Future.successful {
          BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
        }
      },
      data => {
        val userId = request.identity.userID.get
        val delta = data.answer match {
          case true => 1
          case false => -1
        }
        coldStartMap.get(data.coldStartId).get.foreach {
          case (key, value) => userTagRelationDAO.saveIngredientTagRelation(userId, key, value * delta)
        }

        coldStartDAO.saveColdStartResponse(request.identity.userID.get, data)
          .map(x => Ok(Json.obj("ok" -> true, "answer" -> Json.toJson(x))))
      }
    )
  }

  def listColdStarts = SecuredAction.async { implicit request =>
        coldStartDAO.getColdStarts
          .map(x => Ok(Json.obj("ok" -> true, "choices" -> Json.toJson(x))))
  }

}
