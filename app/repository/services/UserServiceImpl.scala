package repository.services

import javax.inject.Inject
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Execution.Implicits._
import com.mohiva.play.silhouette.core.LoginInfo
import com.mohiva.play.silhouette.core.services.AuthInfo
import com.mohiva.play.silhouette.core.providers.CommonSocialProfile
import repository.exceptions.DuplicateException
import repository.models.{UserUpdate, TinyUser, User}
import scala.concurrent.Future
import repository.daos.UserDAO


/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve a user.
   * @return The retrieved user or None if no user could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.find(loginInfo)

  def findUserByUID(uid: Long): Future[Option[User]] = userDAO.find(uid)

  def getAll(offset: Integer, limit: Integer): Future[Seq[TinyUser]] = userDAO.getAll(offset, limit)

  def find(email: String): Future[Option[User]] = userDAO.find(email)

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    userDAO.find(user.email.get).flatMap {
      case Some(u) => throw new DuplicateException("User already exists.")
      case None => userDAO.save(user)
    }
  }

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save[A <: AuthInfo](profile: CommonSocialProfile[A]) = {
    userDAO.find(profile.loginInfo).flatMap {
      case Some(user) => // Update user with profile
        userDAO.save(user.copy(
          firstName = profile.firstName,
          lastName = profile.lastName,
          email = profile.email,
          image = profile.avatarURL,
          role = Some("user")
        ))
      case None => // Insert a new user
        userDAO.save(User(
          userID = None,
          loginInfo = profile.loginInfo,
          firstName = profile.firstName,
          lastName = profile.lastName,
          email = profile.email,
          image = profile.avatarURL,
          role = Some("user"),
          created = Some(new LocalDateTime()),
          recipeLanguage = None,
          appLanguage = None,
          city = None,
          country = None,
          sex = None,
          yearBorn = None,
          enrolled = None,
          metricSystem = None
        ))
    }
  }

  def update(user: UserUpdate, id: Long): Future[Option[User]] = userDAO.update(user, id)
}
