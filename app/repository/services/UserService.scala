package repository.services

import com.mohiva.play.silhouette.core.services.{AuthInfo, IdentityService}
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import repository.models.{UserUpdate, TinyUser, User}
import scala.concurrent.Future

/**
 * Handles actions to users.
 */
trait UserService extends IdentityService[User] {

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save[A <: AuthInfo](profile: CommonSocialProfile[A]): Future[User]

  def update(user: UserUpdate, id: Long): Future[Option[User]]

  def findUserByUID(uid: Long): Future[Option[User]]

  def find(email: String): Future[Option[User]]

  def getAll(offset: Integer, limit: Integer): Future[Seq[TinyUser]]
}
