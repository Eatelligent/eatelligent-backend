package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.silhouette.WithRole
import repository.daos.StatsDAO
import repository.models.{Stats, User}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global

class StatsController @Inject() (
                                   val statsDAO: StatsDAO,
                                   implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] {

  implicit val statsWrites: Writes[Stats] = (
    (JsPath \ "numRecipes").write[Int] and
      (JsPath \ "numIngredients").write[Int] and
      (JsPath \ "numTags").write[Int] and
      (JsPath \ "numUsers").write[Int] and
      (JsPath \ "numStarRatingsRecipe").write[Int] and
      (JsPath \ "numYesNoRatingsRecipe").write[Int] and
      (JsPath\ "numYesNoRatingsIngredient").write[Int]
    )(unlift(Stats.unapply))

  def getTotalStats = SecuredAction(WithRole("admin")).async { implicit request =>
    statsDAO.getTotalStats map {
      s => Ok(Json.obj("ok" -> true, "stats" -> Json.toJson(s)))
    }
  }

}
