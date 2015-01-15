package repository.models

import com.mohiva.play.silhouette.core.{Identity, LoginInfo}
import org.joda.time.LocalDateTime

case class User(
  userID: Option[Long],
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  email: Option[String],
  image: Option[String],
  role: Option[String],
  created: Option[LocalDateTime],
  recipeLanguage: Option[Long],
  appLanguage: Option[Long],
  city: Option[String],
  country: Option[String],
  sex: Option[String],
  yearBorn: Option[Int],
  enrolled: Option[Boolean],
  metricSystem: Option[Boolean]
) extends Identity

case class UserSignUp(
  userId: Option[Long],
  firstName: Option[String],
  lastName: Option[String],
  email: String,
  password: String
                       )

case class TinyUser(
                     id: Long,
                     firstName: Option[String],
                     lastName: Option[String]
                     )


