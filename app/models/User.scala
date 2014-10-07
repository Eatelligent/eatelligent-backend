package models

import java.sql.Date

import myUtils.WithMyDriver
import play.api.libs.json.JsValue

case class User(
                id: Option[Long],
                name: String,
                password: String,
                email: String,
                image: Option[JsValue],
//                birth: Date,
                city: Option[String]
//                created: Option[Date],
//                modified: Option[Date]
                 )

trait UserComponent extends WithMyDriver {
  import driver.simple._

  class Users(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def password = column[String]("password")
    def email = column[String]("email")
    def image = column[Option[JsValue]]("image")
    def birth = column[Option[Date]]("birth")
    def city = column[Option[String]]("city")
    def created = column[Option[Date]]("created")
    def modified = column[Option[Date]]("modified")

    def * = (id, name, password, email, image, city) <> (User.tupled, User.unapply)

  }

  val users = TableQuery[Users]

  private val usersAutoInc = users returning users.map(_.id) into {
    case (u, id) => u.copy(id = id)
  }

  def insert(user: User)(implicit session: Session): User =
    usersAutoInc.insert(user)

  def findUserById(id: Long)(implicit session: Session): Option[User] = {
    users.filter(_.id === id).list.headOption
  }

}