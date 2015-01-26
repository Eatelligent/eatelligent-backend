package models.daos

import com.mohiva.play.silhouette.core.LoginInfo
import repository.models.{UserUpdate, TinyUser, User}
import scala.concurrent.Future
import java.util.UUID

/**
 * Give access to the user object.
 */
trait UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo): Future[Option[User]]

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: Long): Future[Option[User]]

  def find(email: String): Future[Option[User]]

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  def update(user: UserUpdate, id: Long): Future[Option[User]]

  def getAll(offset: Integer, limit: Integer): Future[Seq[TinyUser]]

}
