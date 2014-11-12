package controllers

import play.api.db.slick.DBAction
import play.api.libs.json._
import repository.current.dao._
import repository.current.dao.driver.simple._
import play.api.db.slick._

object TagsController extends MyController {

  def listTags = DBAction { implicit request =>
    val json = Json.toJson(tags.list)
    Ok(Json.obj("ok" -> true, "tags" -> json))
  }

}
