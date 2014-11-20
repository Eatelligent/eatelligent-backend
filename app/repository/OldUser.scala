package repository

import java.sql.Date

import myUtils.WithMyDriver
import play.api.libs.json.JsValue

case class MyUser(
                id: Option[Long],
                name: String,
                password: String,
                email: String,
                image: Option[JsValue],
//                birth: Date,
                city: Option[String],
                roleId: Long
//                created: Option[Date],
//                modified: Option[Date]
                 )

case class TinyUser(
                    id: String,
                    firstName: Option[String],
                    lastName: Option[String]
                     )
case class Role (
                  id: Long,
                  name: String
             )

//trait UserComponent extends WithMyDriver {
//  import driver.simple._
//
//  class OldUsers(tag: Tag) extends Table[MyUser](tag, "users") {
//    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("name")
//    def password = column[String]("password")
//    def email = column[String]("email")
//    def image = column[Option[JsValue]]("image")
//    def birth = column[Option[Date]]("birth")
//    def city = column[Option[String]]("city")
//    def created = column[Option[Date]]("created")
//    def modified = column[Option[Date]]("modified")
//    def roleId = column[Long]("role")
//    def * = (id, name, password, email, image, city, roleId) <> (MyUser.tupled, MyUser.unapply)
//    def role = foreignKey("roles", roleId, roles)(_.id)
//  }
//
//  val users = TableQuery[OldUsers]
//
//  private val usersAutoInc = users returning users.map(_.id) into {
//    case (u, id) => u.copy(id = id)
//  }
//
//  def insert(user: MyUser)(implicit session: Session): MyUser =
//    usersAutoInc.insert(user)
//
//  def findUserById(id: Long)(implicit session: Session): Option[MyUser] = {
//    users.filter(_.id === id).list.headOption
//  }
//
//  def findRoleById(id: Long)(implicit session: Session): Option[Role] = {
//    roles.filter(_.id === id).list.headOption
//  }
//
//
//  class Roles(tag: Tag) extends Table[Role](tag, "roles") {
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("name")
//    def * = (id, name) <> (Role.tupled, Role.unapply)
//  }
//
//  val roles = TableQuery[Roles]
//
//}