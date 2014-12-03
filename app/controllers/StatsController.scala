package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import myUtils.silhouette.WithRole
import org.joda.time.LocalDateTime
import repository.daos.StatsDAO
import repository.models.User
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

class StatsController @Inject() (
                                   val statsDAO: StatsDAO,
                                   implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def getTotalStats = SecuredAction(WithRole("admin")).async { implicit request =>
    statsDAO.getTotalStats map {
      s => Ok(Json.obj("ok" -> true, "stats" -> Json.toJson(s)))
    }
  }

  def getUserStats(from: String, to: String) = SecuredAction(WithRole("admin")).async {
    implicit request =>
    statsDAO.getUserStats(new LocalDateTime(from), new LocalDateTime(to)) map {
      s => Ok(Json.obj("ok" -> true, "stats" -> Json.toJson(s)))
    }
  }

  def getRatingStats(from: String, to: String) = SecuredAction(WithRole("admin")).async {
    implicit request =>
      statsDAO.getRatingStats(new LocalDateTime(from), new LocalDateTime(to)) map {
        s => Ok(Json.obj("ok" -> true, "stats" -> Json.toJson(s)))
      }
  }

}
