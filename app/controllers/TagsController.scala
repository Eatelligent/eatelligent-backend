package controllers

import com.google.inject.Inject
import play.api.libs.json._
import play.api.mvc.Action
import repository.services.TagService
import play.api.libs.concurrent.Execution.Implicits._

class TagsController @Inject() (
  val tagService: TagService
)  extends MyController {

  def listTags = Action.async { implicit request =>
    val tags = tagService.getAll
    tags.map(ts => Ok(Json.obj("ok" -> true, "tags" -> Json.toJson(ts))))
  }


}
