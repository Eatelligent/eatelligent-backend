package models.daos.slick

import java.sql.Timestamp

import org.joda.time.DateTime
import play.api.libs.json.{Json, JsValue}
import play.api.data.format.Formats
import play.api.data.format.Formatter
import play.api.data.FormError
import myUtils.MyPostgresDriver.simple._
import com.vividsolutions.jts.io.{WKTReader, WKTWriter}
object DBTableDefinitions {

  implicit def date2dateTime = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    date => new DateTime(date))

  def jsonFormat: Formatter[JsValue] = new Formatter[JsValue] {
    override val format = Some(("format.json", Nil))

    def bind(key: String, data: Map[String, String]) =
      parsing(Json.parse(_), "error.json", Nil)(key, data)
    def unbind(key: String, value: JsValue) = Map(key -> Json.stringify(value))
  }

  private def parsing[T](parse: String => T, errMsg: String, errArgs: Seq[Any])(
    key: String, data: Map[String, String]): Either[Seq[FormError], T] = {
    Formats.stringFormat.bind(key, data).right.flatMap { s =>
      scala.util.control.Exception.allCatch[T]
        .either(parse(s))
        .left.map(e => Seq(FormError(key, errMsg, errArgs)))
    }
  }

  case class DBRecipe(
                           id: Option[Long],
                           name: String,
                           image: Option[String],
                           description: String,
                           language: Int,
                           calories: Double,
                           procedure: String,
                           spicy: Int,
                           created: DateTime,
                           modified: DateTime,
                           createdById: Long
                           )

  class Recipes(tag: Tag) extends Table[DBRecipe](tag, "recipe") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[String]]("image")
    def description = column[String]("description")
    def language = column[Int]("language")
    def calories = column[Double]("calories")
    def procedure = column[String]("procedure")
    def spicy = column[Int]("spicy")
    def created = column[DateTime]("created")
    def modified = column[DateTime]("modified")
    def createdById = column[Long]("created_by")

    def * = (id, name, image, description, language, calories, procedure, spicy, created, modified,
      createdById) <> (DBRecipe.tupled,
      DBRecipe.unapply)
  }

  case class DBIngredient(
                               id: Option[Long],
                               name: String,
                               image: Option[JsValue]
                               )

  class Ingredients(tag: Tag) extends Table[DBIngredient](tag, "ingredient") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")

    def * = (id, name, image) <> (DBIngredient.tupled, DBIngredient.unapply)
  }

  case class DBIngredientInRecipe(
                                       recipeId: Long,
                                       ingredientId: Long,
                                       amount: Double
                                       )

  class IngredientsInRecipe(tag: Tag) extends Table[DBIngredientInRecipe](tag, "ingredient_in_recipe") {
    def recipeId = column[Long]("recipe_id")
    def ingredientId = column[Long]("ingredient_id")
    def amount = column[Double]("amount")
    def * = (recipeId, ingredientId, amount) <> (DBIngredientInRecipe.tupled, DBIngredientInRecipe.unapply)
  }


  case class DBTag(
                        id: Option[Long],
                        name: String
                        )

  class Tags(tag: Tag) extends Table[DBTag](tag, "tags") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")

    def * = (id, name) <> (DBTag.tupled, DBTag.unapply)
  }


  case class DBTagForRecipe(
                           recipeId: Long,
                           tagId: Long
                           )

  class TagsForRecipe(tag: Tag) extends Table[DBTagForRecipe](tag, "recipe_in_tag") {
    def recipeId = column[Long]("recipe_id")
    def tagId = column[Long]("tag_id")

    def * = (recipeId, tagId) <> (DBTagForRecipe.tupled, DBTagForRecipe.unapply)
  }

  case class DBUser (
    userID: String,
    firstName: Option[String],
    lastName: Option[String],
    email: Option[String],
    image: Option[String]
  )

  class Users(tag: Tag) extends Table[DBUser](tag, "users") {
    def id = column[String]("id", O.PrimaryKey)
    def firstName = column[Option[String]]("first_name")
    def lastName = column[Option[String]]("last_name")
    def email = column[Option[String]]("email")
    def image = column[Option[String]]("image")
    def * = (id, firstName, lastName, email, image) <> (DBUser.tupled, DBUser.unapply)
  }

  case class DBLoginInfo (
    id: Option[Long],
    providerID: String,
    providerKey: String
  )

  class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "logininfo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def providerID = column[String]("provider_id")
    def providerKey = column[String]("provider_key")
    def * = (id.?, providerID, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
  }

  case class DBUserLoginInfo (
    userID: String,
    loginInfoId: Long
  )

  class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "userlogininfo") {
    def userID = column[String]("user_id", O.NotNull)
    def loginInfoId = column[Long]("login_info_id", O.NotNull)
    def * = (userID, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
  }

  case class DBPasswordInfo (
    hasher: String,
    password: String,
    salt: Option[String],
    loginInfoId: Long
  )

  class PasswordInfos(tag: Tag) extends Table[DBPasswordInfo](tag, "passwordinfo") {
    def hasher = column[String]("hasher")
    def password = column[String]("password")
    def salt = column[Option[String]]("salt")
    def loginInfoId = column[Long]("login_info_id")
    def * = (hasher, password, salt, loginInfoId) <> (DBPasswordInfo.tupled, DBPasswordInfo.unapply)
  }

  case class DBOAuth1Info (
    id: Option[Long],
    token: String,
    secret: String,
    loginInfoId: Long
  )

  class OAuth1Infos(tag: Tag) extends Table[DBOAuth1Info](tag, "oauth1info") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def token = column[String]("token")
    def secret = column[String]("secret")
    def loginInfoId = column[Long]("login_info_id")
    def * = (id.?, token, secret, loginInfoId) <> (DBOAuth1Info.tupled, DBOAuth1Info.unapply)
  }

  case class DBOAuth2Info (
    id: Option[Long],
    accessToken: String,
    tokenType: Option[String],
    expiresIn: Option[Int],
    refreshToken: Option[String],
    loginInfoId: Long
  )

  class OAuth2Infos(tag: Tag) extends Table[DBOAuth2Info](tag, "oauth2info") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def accessToken = column[String]("accesstoken")
    def tokenType = column[Option[String]]("tokentype")
    def expiresIn = column[Option[Int]]("expiresin")
    def refreshToken = column[Option[String]]("refreshtoken")
    def loginInfoId = column[Long]("login_info_id")
    def * = (id.?, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <> (DBOAuth2Info.tupled, DBOAuth2Info.unapply)
  }

  val slickUsers = TableQuery[Users]
  val slickLoginInfos = TableQuery[LoginInfos]
  val slickUserLoginInfos = TableQuery[UserLoginInfos]
  val slickPasswordInfos = TableQuery[PasswordInfos]
  val slickOAuth1Infos = TableQuery[OAuth1Infos]
  val slickOAuth2Infos = TableQuery[OAuth2Infos]
  val slickRecipes = TableQuery[Recipes]
  val slickTags = TableQuery[Tags]
  val slickTagsForRecipe = TableQuery[TagsForRecipe]
  val slickIngredients = TableQuery[Ingredients]
  val slickIngredientsInRecipe = TableQuery[IngredientsInRecipe]

  def insertTag(tag: DBTag)(implicit session: Session): Long = {
    (slickTags returning slickTags.map(_.id) += tag).toList.head
  }

  def insertRecipe(recipe: DBRecipe)(implicit session: Session): Long = {
    (slickRecipes returning slickRecipes.map(_.id) += recipe).toList.head
  }

  def insertIngredient(ingredient: DBIngredient)(implicit session: Session): Long = {
    (slickIngredients returning slickIngredients.map(_.id) += ingredient).toList.head
  }

}
