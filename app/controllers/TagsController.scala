package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.models.{User, RecipeTag}
import repository.services.TagService
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

class TagsController @Inject() (
  val tagService: TagService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def listTags = SecuredAction.async { implicit request =>
    val tags = tagService.getAll
    tags.map(ts => Ok(Json.obj("ok" -> true, "tags" -> Json.toJson(ts))))
  }


}
