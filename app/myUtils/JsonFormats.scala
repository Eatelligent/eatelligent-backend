package myUtils

import java.util.UUID

import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.providers.Credentials
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import repository.models._

trait JsonFormats {

  implicit val statsWrites: Writes[Stats] = (
    (JsPath \ "numRecipes").write[Int] and
      (JsPath \ "numIngredients").write[Int] and
      (JsPath \ "numTags").write[Int] and
      (JsPath \ "numUsers").write[Int] and
      (JsPath \ "numStarRatingsRecipe").write[Int] and
      (JsPath \ "numYesNoRatingsRecipe").write[Int] and
      (JsPath\ "numYesNoRatingsIngredient").write[Int]
    )(unlift(Stats.unapply))

  implicit val jodaDateWrites: Writes[LocalDateTime] = new Writes[LocalDateTime]   {
    def writes(d: LocalDateTime): JsValue = JsString(d.toString)
  }

  implicit val readsJodaLocalDateTime = Reads[LocalDateTime](js =>
    js.validate[String].map[LocalDateTime](dtString =>
      LocalDateTime.parse(dtString, ISODateTimeFormat.basicDateTime())
    )
  )

  implicit val dateStatsWrites: Writes[DateStats] = (
    (JsPath \ "date").write[LocalDateTime] and
      (JsPath \ "number").write[Int]
    )(unlift(DateStats.unapply))

  implicit val credentialsReads: Reads[Credentials] = (
    (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
    )(Credentials.apply _)

  implicit val signUpRead: Reads[UserSignUp] = (
    (JsPath \ "id").readNullable[UUID] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "password").read[String]
    )(UserSignUp.apply _)

  implicit val signUpWrite: Writes[UserSignUp] = (
    (JsPath \ "id").write[Option[UUID]] and
      (JsPath \ "firstName").write[String] and
      (JsPath \ "lastName").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "password").write[String]
    )(unlift(UserSignUp.unapply))

  implicit val loginInfoReads: Reads[LoginInfo] = (
    (JsPath \ "providerID").read[String] and
      (JsPath \ "providerKey").read[String]
    )(LoginInfo.apply _)

  implicit val loginInfoWrites: Writes[LoginInfo] = (
    (JsPath \ "providerID").write[String] and
      (JsPath \ "providerKey").write[String]
    )(unlift(LoginInfo.unapply))

  implicit val userRead: Reads[User] = (
    (JsPath \ "id").read[UUID] and
      (JsPath \ "loginInfo").read[LoginInfo] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String] and
      (JsPath \ "email").readNullable[String] and
      (JsPath \ "image").readNullable[String] and
      (JsPath \ "role").readNullable[String] and
      (JsPath \ "created").readNullable[LocalDateTime]
    )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[UUID] and
      (JsPath \ "loginInfo").write[LoginInfo] and
      (JsPath \ "firstName").write[Option[String]] and
      (JsPath \ "lastName").write[Option[String]] and
      (JsPath \ "email").write[Option[String]] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "role").write[Option[String]] and
      (JsPath \ "created").write[Option[LocalDateTime]]
    )(unlift(User.unapply))

  implicit val tinyUserRead: Reads[TinyUser] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "firstName").readNullable[String] and
      (JsPath \ "lastName").readNullable[String]
    )(TinyUser.apply _)

  implicit val tinyUserWrite: Writes[TinyUser] = (
    (JsPath \ "id").write[String] and
      (JsPath \ "firstName").write[Option[String]] and
      (JsPath \ "lastName").write[Option[String]]
    )(unlift(TinyUser.unapply))

  implicit val recipeImageReads: Writes[RecipeImage] = (
    (JsPath \ "recipeId").write[Long] and
      (JsPath \ "url").write[String]
    )(unlift(RecipeImage.unapply))

  implicit val languageWrites: Writes[Language] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "locale").write[String] and
      (JsPath \ "name").write[String]
    )(unlift(Language.unapply))

  implicit val languageRead: Reads[Language] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "locale").read[String] and
      (JsPath \ "name").read[String]
    )(Language.apply _)

  implicit val userStarRateRecipeRead: Reads[UserStarRateRecipe] = (
    (JsPath \ "userId").read[String] and
      (JsPath \ "recipeId").read[Long] and
      (JsPath \ "rating").read[Double] and
      (JsPath \ "created").readNullable[LocalDateTime]
    )(UserStarRateRecipe.apply _)

  implicit val userStarRateRecipeWrite: Writes[UserStarRateRecipe] = (
    (JsPath \ "userId").write[String] and
      (JsPath \ "recipeId").write[Long] and
      (JsPath \ "rating").write[Double] and
      (JsPath \ "created").write[Option[LocalDateTime]]
    )(unlift(UserStarRateRecipe.unapply))

  implicit val tagWrite: Writes[RecipeTag] =(
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String]
    )(unlift(RecipeTag.unapply))

  implicit val ingredientForRecipeRead: Reads[IngredientForRecipe] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[JsValue] and
      (JsPath \ "amount").read[Double]
    //    (JsPath \ "unit").readNullable[Unit]
    )(IngredientForRecipe.apply _)

  implicit val ingredientForRecipeWrites: Writes[IngredientForRecipe] = (
    (JsPath \ "id").writeNullable[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[JsValue]] and
      (JsPath \ "amount").write[Double]
    //        (JsPath \ "unit").writeNullable[Unit]
    )(unlift(IngredientForRecipe.unapply))

  implicit val recipeRead: Reads[Recipe] = (
    (JsPath \ "id").readNullable[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").readNullable[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "language").read[Int] and
      (JsPath \ "calories").readNullable[Double] and
      (JsPath \ "procedure").read[String] and
      (JsPath \ "spicy").read[Int] and
      (JsPath \ "time").read[Int] and
      (JsPath \ "created").readNullable[LocalDateTime] and
      (JsPath \ "modified").readNullable[LocalDateTime] and
      (JsPath \ "published").readNullable[LocalDateTime] and
      (JsPath \ "deleted").readNullable[LocalDateTime] and
      (JsPath \ "ingredients").read[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").read[Seq[String]] and
      (JsPath \ "createdBy").readNullable[TinyUser]
    )(Recipe.apply _)

  implicit val recipeWrites: Writes[Recipe] = (
    (JsPath \ "id").write[Option[Long]] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]] and
      (JsPath \ "description").write[String] and
      (JsPath \ "language").write[Int] and
      (JsPath \ "calories").write[Option[Double]] and
      (JsPath \ "procedure").write[String] and
      (JsPath \ "spicy").write[Int] and
      (JsPath \ "time").write[Int] and
      (JsPath \ "created").write[Option[LocalDateTime]] and
      (JsPath \ "modified").write[Option[LocalDateTime]] and
      (JsPath \ "published").write[Option[LocalDateTime]] and
      (JsPath \ "deleted").write[Option[LocalDateTime]] and
      (JsPath \ "ingredients").write[Seq[IngredientForRecipe]] and
      (JsPath \ "tags").write[Seq[String]] and
      (JsPath \ "createdBy").write[Option[TinyUser]]
    )(unlift(Recipe.unapply))

  implicit val tinyRecipeRead: Reads[TinyRecipe] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "image").read[Option[String]]
    )(TinyRecipe.apply _)

  implicit val tinyRecipeWrites: Writes[TinyRecipe] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "name").write[String] and
      (JsPath \ "image").write[Option[String]]
    )(unlift(TinyRecipe.unapply))
}