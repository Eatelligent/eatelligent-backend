package repository.daos.slick

import org.joda.time.LocalDateTime
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import recommandation.coldstart.UserColdStartModel
import repository.daos.slick.DBTableDefinitions._
import com.mohiva.play.silhouette.core.LoginInfo
import repository.exceptions.NoSuchUserException
import repository.models.{UserUpdate, TinyUser, User}
import scala.concurrent.Future
import play.Logger
import repository.daos.UserDAO
import scala.concurrent.ExecutionContext.Implicits.global
import scala.slick.jdbc.{GetResult, StaticQuery => Q}


/**
 * Give access to the user object using Slick
 */
class UserDAOSlick extends UserDAO {

  import play.api.Play.current

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo) = {
    Future.successful {
      DB withSession { implicit session =>
        slickLoginInfos.filter(
          x => x.providerID === loginInfo.providerID && x.providerKey === loginInfo.providerKey
        ).firstOption match {
          case Some(info) =>
            slickUserLoginInfos.filter(_.loginInfoId === info.id).firstOption match {
              case Some(userLoginInfo) =>
                slickUsers.filter(_.id === userLoginInfo.userID).firstOption match {
                  case Some(user) =>
                    Some(User(
                      user.userID,
                      loginInfo,
                      user.firstName,
                      user.lastName,
                      user.email,
                      user.image,
                      user.role,
                      Some(user.created),
                      user.recipeLanguage,
                      user.appLanguage,
                      user.city,
                      user.country,
                      user.sex,
                      user.yearBorn,
                      Some(user.enrolled),
                      Some(user.metricSystem)
                    ))
                  case None => None
                }
              case None => None
            }
          case None => None
        }
      }
    }
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def find(userID: Long): Future[Option[User]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUsers.filter(
          _.id === userID
        ).firstOption match {
          case Some(user) =>
            slickUserLoginInfos.filter(_.userID === user.userID).firstOption match {
              case Some(info) =>
                slickLoginInfos.filter(_.id === info.loginInfoId).firstOption match {
                  case Some(loginInfo) =>
                    Some(User(
                      user.userID,
                      LoginInfo(loginInfo.providerID, loginInfo.providerKey),
                      user.firstName,
                      user.lastName,
                      user.email,
                      user.image,
                      user.role,
                      Some(user.created),
                      user.recipeLanguage,
                      user.appLanguage,
                      user.city,
                      user.country,
                      user.sex,
                      user.yearBorn,
                      Some(user.enrolled),
                      Some(user.metricSystem)
                    ))
                  case None => None
                }
              case None => None
            }
          case None => None
        }
      }
    }
  }

  def find(email: String): Future[Option[User]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUsers.filter(
          _.email === email
        ).firstOption match {
          case Some(user) =>
            slickUserLoginInfos.filter(_.userID === user.userID).firstOption match {
              case Some(info) =>
                slickLoginInfos.filter(_.id === info.loginInfoId).firstOption match {
                  case Some(loginInfo) =>
                    Some(User(
                      user.userID,
                      LoginInfo(loginInfo.providerID, loginInfo.providerKey),
                      user.firstName,
                      user.lastName,
                      user.email,
                      user.image,
                      user.role,
                      Some(user.created),
                      user.recipeLanguage,
                      user.appLanguage,
                      user.city,
                      user.country,
                      user.sex,
                      user.yearBorn,
                      Some(user.enrolled),
                      Some(user.metricSystem)
                    ))
                  case None => None
                }
              case None => None
            }
          case None => None
        }
      }
    }
  }

  def update(user: UserUpdate, id: Long): Future[Option[User]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUsers.filter(_.id === id).firstOption match {
          case Some(u) =>
            slickUsers.filter(_.id === id).update(
              DBUser(
                Some(id),
                user.firstName,
                user.lastName,
                user.email,
                user.image,
                u.role,
                u.created,
                user.recipeLanguage,
                user.appLanguage,
                user.city,
                user.country,
                user.sex,
                user.yearBorn,
                user.enrolled match {
                  case Some(e) => e
                  case None => false
                },
                user.metricSystem match {
                  case Some(m) => m
                  case None => true
                }
              )
            )
          case None => throw new NoSuchUserException(id)
        }
      }
    }
    find(id)
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User] = {
    Future.successful {
      DB withSession { implicit session =>
        val dbUser =
          DBUser(user.userID,
            user.firstName,
            user.lastName,
            user.email,
            user.image,
            user.role,
            user.created match {
              case Some(time) => time
              case None => new LocalDateTime()
            }, user.recipeLanguage,
            user.appLanguage,
            user.city,
            user.country,
            user.sex,
            user.yearBorn,
            user.enrolled match {
              case Some(e) => e
              case None => false
            },
            user.metricSystem match {
              case Some(m) => m
              case None => true
            }
          )
        val id: Long = slickUsers.filter(_.email === dbUser.email).firstOption match {
          case Some(userFound) => slickUsers.filter(_.id === dbUser.userID).update(dbUser)
            userFound.userID.get
          case None => insertUser(dbUser)
        }
        var dbLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
        // Insert if it does not exist yet
        slickLoginInfos.filter(info => info.providerID === dbLoginInfo.providerID && info.providerKey === dbLoginInfo.providerKey).firstOption match {
          case None => slickLoginInfos.insert(dbLoginInfo)
          case Some(info) => Logger.debug("Nothing to insert since info already exists: " + info)
        }
        dbLoginInfo = slickLoginInfos.filter(info => info.providerID === dbLoginInfo.providerID && info.providerKey === dbLoginInfo.providerKey).first
        // Now make sure they are connected
        slickUserLoginInfos.filter(info => info.userID === id && info.loginInfoId === dbLoginInfo.id).firstOption
        match {
          case Some(info) =>
            // They are connected already, we could as well omit this case ;)
          case None =>
            slickUserLoginInfos += DBUserLoginInfo(id, dbLoginInfo.id.get)
        }
      }
    }
    find(user.email.get) map (u => u.get)// We do not change the user => return it
  }

  def getAll(offset: Integer, limit: Integer): Future[Seq[TinyUser]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUsers.sortBy(_.id.desc).drop(offset).take(limit).list map {
          u => TinyUser(u.userID.get, u.firstName, u.lastName)
        }
      }
    }
  }

  implicit val getColdstartResults = GetResult(c => UserColdStartModel(c.nextLong(), c.nextBooleanArray()))

  def getAllUserColdStarts(limit: Int): Seq[UserColdStartModel] = {
    DB withSession { implicit session =>
      Q.queryNA[UserColdStartModel](
        "SELECT user_id, array_agg(answer) " +
        "FROM user_cold_start " +
          "GROUP BY user_id " +
          "LIMIT " + limit + ";"
      ).list
    }
  }

  def getUserColdStarts(userId: Long): Option[UserColdStartModel] = {
    DB withSession { implicit session =>
      Q.queryNA[UserColdStartModel](
        "SELECT user_id, array_agg(answer) " +
          "FROM user_cold_start " +
          "GROUP BY user_id " +
          "LIMIT 1;"
      ).list.headOption
    }
  }
}
