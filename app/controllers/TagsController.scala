package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import play.api.libs.json._
import repository.daos.TagDAO
import repository.models.{User, RecipeTag}
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

class TagsController @Inject() (
  val tagDAO: TagDAO,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def findTags(q: Option[String]) = SecuredAction.async { implicit request =>
    val tags = q match {
      case Some(query) => tagDAO.find(query)
      case None => tagDAO.getAll
    }
    tags.map(ts => Ok(Json.obj("ok" -> true, "tags" -> Json.toJson(ts))))
  }


}
