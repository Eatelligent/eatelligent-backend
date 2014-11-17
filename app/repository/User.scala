package models

import com.mohiva.play.silhouette.core.{LoginInfo, Identity}
import java.util.UUID

/**
 * The user object.
 *
 * @param userID The unique ID of the user.
 * @param loginInfo The linked login info.
 * @param firstName Maybe the first name of the authenticated user.
 * @param lastName Maybe the last name of the authenticated user.
 * @param email Maybe the email of the authenticated provider.
 * @param image Maybe the avatar URL of the authenticated provider.
 */
case class User(
  userID: UUID,
  loginInfo: LoginInfo,
  firstName: Option[String],
  lastName: Option[String],
  email: Option[String],
  image: Option[String]
) extends Identity
