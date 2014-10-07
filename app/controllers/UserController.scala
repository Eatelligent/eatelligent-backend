package controllers

import models.User
import play.api.db.slick.DBAction
import play.api.mvc.Controller
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.current.dao._
import models.current.dao.driver.simple._
import play.api.db.slick._
import play.api.mvc._
import play.api.Play.current

object UserController extends Controller {
  
  implicit val userRead: Reads[User] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "city").readNullable[String]
    )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \"image").write[Option[JsValue]] and
      (JsPath \ "city").write[Option[String]]
    )(unlift(User.unapply))

  def listUsers = DBAction { implicit request =>
    val json = Json.toJson(users.list)
    Ok(Json.obj("ok" -> true, "users" -> json))
  }

  def saveUser = DBAction(BodyParsers.parse.json) { implicit request =>
    val userResult = request.body.validate[User]
    userResult.fold(
      errors => {
        BadRequest(Json.obj("ok" -> false, "message" -> JsError.toFlatJson(errors)))
      },
      user => {
        users.insert(user)
        Ok(Json.obj("ok" -> true, "message" -> ("User '" + user.name + "' saved,")))
      }
    )
  }

  def getUser(id: Long) = DBAction { implicit session =>
    val json = Json.toJson(findUserById(id))
    Ok(Json.obj("ok" -> true, "user" -> json))
  }

}
