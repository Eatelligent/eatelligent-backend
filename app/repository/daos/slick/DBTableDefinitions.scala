package repository.daos.slick

import org.joda.time.LocalDateTime
import play.api.libs.json.{Json, JsValue}
import myUtils.MyPostgresDriver.simple._
object DBTableDefinitions {


  case class DBLanguage(
                       id: Option[Long] = None,
                       locale: String,
                       name: String
                       )

  class Languages(tag: Tag) extends Table[DBLanguage](tag, "language") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def locale = column[String]("locale")
    def name = column[String]("name")

    def * = (id, locale, name) <> (DBLanguage.tupled, DBLanguage.unapply)
  }


  case class DBRecipe(
                           id: Option[Long],
                           name: String,
                           image: Option[String],
                           description: Option[String],
                           language: Long,
                           calories: Option[Double],
                           procedure: Option[String],
                           spicy: Option[Int],
                           time: Option[Int],
                           source: Option[String],
                           difficulty: Option[String],
                           created: LocalDateTime,
                           modified: LocalDateTime,
                           published: Option[LocalDateTime],
                           deleted: Option[LocalDateTime],
                           createdById: Long
                           )

  class Recipes(tag: Tag) extends Table[DBRecipe](tag, "recipe") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[String]]("image")
    def description = column[Option[String]]("description")
    def language = column[Long]("language")
    def calories = column[Option[Double]]("calories")
    def procedure = column[Option[String]]("procedure")
    def spicy = column[Option[Int]]("spicy")
    def time = column[Option[Int]]("time")
    def source = column[Option[String]]("source")
    def difficulty = column[Option[String]]("difficulty")
    def created = column[LocalDateTime]("created")
    def modified = column[LocalDateTime]("modified")
    def published = column[Option[LocalDateTime]]("published")
    def deleted = column[Option[LocalDateTime]]("deleted")
    def createdById = column[Long]("created_by")

    def * = (id, name, image, description, language, calories, procedure, spicy, time, difficulty, source, created,
      modified,
      published, deleted, createdById) <> (DBRecipe.tupled, DBRecipe.unapply)
  }

  case class DBUnit(
                   id: Option[Long],
                   name: String
                     )

  class Units(tag: Tag) extends Table[DBUnit](tag, "unit") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> (DBUnit.tupled, DBUnit.unapply)
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
                                       unitId: Long,
                                       amount: Double
                                       )

  class IngredientsInRecipe(tag: Tag) extends Table[DBIngredientInRecipe](tag, "ingredient_in_recipe") {
    def recipeId = column[Long]("recipe_id")
    def ingredientId = column[Long]("ingredient_id")
    def unitId = column[Long]("unit_id")
    def amount = column[Double]("amount")
    def * = (recipeId, ingredientId, unitId, amount) <> (DBIngredientInRecipe.tupled, DBIngredientInRecipe.unapply)
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
    userID: Option[Long],
    firstName: Option[String],
    lastName: Option[String],
    email: Option[String],
    image: Option[String],
    role: Option[String],
    created: LocalDateTime,
    recipeLanguage: Option[Long],
    appLanguage: Option[Long],
    city: Option[String],
    country: Option[String],
    sex: Option[String],
    yearBorn: Option[Int],
    enrolled: Boolean,
    metricSystem: Boolean
  )

  class Users(tag: Tag) extends Table[DBUser](tag, "users") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[Option[String]]("first_name")
    def lastName = column[Option[String]]("last_name")
    def email = column[Option[String]]("email")
    def image = column[Option[String]]("image")
    def role = column[Option[String]]("role")
    def created = column[LocalDateTime]("created")
    def recipeLanguage = column[Option[Long]]("recipe_language")
    def appLanguage = column[Option[Long]]("app_language")
    def city = column[Option[String]]("city")
    def country = column[Option[String]]("country")
    def sex = column[Option[String]]("sex")
    def yearBorn = column[Option[Int]]("year_born")
    def enrolled = column[Boolean]("enrolled")
    def metricSystem = column[Boolean]("metric_system")
    def * = (id, firstName, lastName, email, image, role, created, recipeLanguage, appLanguage, city, country, sex,
      yearBorn, enrolled, metricSystem) <> (DBUser
      .tupled, DBUser
      .unapply)
  }

  case class DBFavorites (
                         userId: Long,
                         recipeId: Long
                           )

  class Favorites(tag: Tag) extends Table[DBFavorites](tag, "favorites") {
    def userId = column[Long]("user_id")
    def recipeId = column[Long]("recipe_id")
    def * = (userId, recipeId) <> (DBFavorites.tupled, DBFavorites.unapply)
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
    userID: Long,
    loginInfoId: Long
  )

  class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "userlogininfo") {
    def userID = column[Long]("user_id", O.NotNull)
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


  case class DBUserStarRateRecipe(
    userId: Long,
    recipeId: Long,
    stars: Double,
    created: LocalDateTime,
    createdLong: Long
                                   )

  class UserStarRateRecipes(tag: Tag) extends Table[DBUserStarRateRecipe](tag, "user_star_rate_recipe") {
    def userId = column[Long]("user_id")
    def recipeId = column[Long]("recipe_id")
    def rating = column[Double]("rating")
    def created = column[LocalDateTime]("created")
    def createdLong = column[Long]("created_long")
    def * = (userId, recipeId, rating, created, createdLong) <> (DBUserStarRateRecipe.tupled, DBUserStarRateRecipe
      .unapply)
  }

  case class DBUserYesNoRateRecipe(
                                   userId: Long,
                                   recipeId: Long,
                                   rating: Boolean,
                                   created: LocalDateTime
                                   )

  class UserYesNoRateRecipes(tag: Tag) extends Table[DBUserYesNoRateRecipe](tag, "user_yes_no_rate_recipe") {
    def userId = column[Long]("user_id")
    def recipeId = column[Long]("recipe_id")
    def rating = column[Boolean]("rating")
    def created = column[LocalDateTime]("created")
    def * = (userId, recipeId, rating, created) <> (DBUserYesNoRateRecipe.tupled, DBUserYesNoRateRecipe.unapply)
  }

  case class DBUserYesNoRateIngredient(
                                    userId: Long,
                                    recipeId: Long,
                                    rating: Boolean,
                                    created: LocalDateTime
                                    )

  class UserYesNoRateIngredients(tag: Tag) extends Table[DBUserYesNoRateIngredient](tag,
    "user_yes_no_rate_ingredient") {
    def userId = column[Long]("user_id")
    def ingredientId = column[Long]("ingredient_id")
    def rating = column[Boolean]("rating")
    def created = column[LocalDateTime]("created")
    def * = (userId, ingredientId, rating, created) <> (DBUserYesNoRateIngredient.tupled, DBUserYesNoRateIngredient
      .unapply)
  }

  case class DBIngredientTag(
                            id: Option[Long],
                            name: String
                              )

  class IngredientTags(tag: Tag) extends Table[DBIngredientTag](tag, "ingredient_tag") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (id, name) <> (DBIngredientTag.tupled, DBIngredientTag.unapply)
  }

  case class DBIngredientInTag(
                              ingredientId: Long,
                              tagId: Long
                                )

  class IngredientsInTags(tag: Tag) extends Table[DBIngredientInTag](tag, "ingredient_in_tag") {
    def ingredientId = column[Long]("ingredient_id")
    def tagId = column[Long]("tag_id")
    def * = (ingredientId, tagId) <> (DBIngredientInTag.tupled, DBIngredientInTag.unapply)
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
  val slickLanguages = TableQuery[Languages]
  val slickUserStarRateRecipes = TableQuery[UserStarRateRecipes]
  val slickUserYesNoRateRecipes = TableQuery[UserYesNoRateRecipes]
  val slickUserYesNoRateIngredient = TableQuery[UserYesNoRateIngredients]
  val slickUnits = TableQuery[Units]
  val slickFavorites = TableQuery[Favorites]
  val slickIngredientTags = TableQuery[IngredientTags]
  val slickIngredientInTags = TableQuery[IngredientsInTags]


  def insertTag(tag: DBTag)(implicit session: Session): Long = {
    (slickTags returning slickTags.map(_.id) += tag).toList.head
  }

  def insertRecipe(recipe: DBRecipe)(implicit session: Session): Long = {
    (slickRecipes returning slickRecipes.map(_.id) += recipe).toList.head
  }

  def insertIngredient(ingredient: DBIngredient)(implicit session: Session): Long = {
    (slickIngredients returning slickIngredients.map(_.id) += ingredient).toList.head
  }

  def insertLanguage(language: DBLanguage)(implicit  session: Session): Long = {
    (slickLanguages returning slickLanguages.map(_.id) += language).toList.head
  }

  def insertUser(user: DBUser)(implicit session: Session): Long = {
    (slickUsers returning slickUsers.map(_.id) += user).toList.head
  }

  def insertIngredientTag(tag: DBIngredientTag)(implicit session: Session): Long = {
    (slickIngredientTags returning slickIngredientTags.map(_.id) += tag).get
  }

}
