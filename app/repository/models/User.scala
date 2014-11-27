package repository.models

import java.util.UUID

import com.mohiva.play.silhouette.core.{Identity, LoginInfo}

case class User(
  userID: UUID,
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  email: Option[String],
  image: Option[String],
  role: Option[String]
) extends Identity

case class UserSignUp(
  userId: Option[UUID],
  firstName: String,
  lastName: String,
  email: String,
  password: String
                       )

case class TinyUser(
                     id: String,
                     firstName: Option[String],
                     lastName: Option[String]
                     )


