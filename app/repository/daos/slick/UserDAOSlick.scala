package models.daos.slick

import org.joda.time.{LocalDateTime, DateTime}
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import com.mohiva.play.silhouette.core.LoginInfo
import repository.Exceptions.NoSuchUserException
import repository.models.{UserUpdate, TinyUser, User}
import scala.concurrent.Future
import java.util.UUID
import play.Logger
import models.daos.UserDAO

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
    DB withSession { implicit session =>
      Future.successful {
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
    DB withSession { implicit session =>
      Future.successful {
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

  def update(user: UserUpdate, id: Long): Future[Option[User]] = {
    DB withSession { implicit session =>
      Future.successful {
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
      find(id)
    }
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    DB withSession { implicit session =>
      Future.successful {
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
        user // We do not change the user => return it
      }
    }
  }

  def getAll(offset: Integer, limit: Integer): Future[Seq[TinyUser]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUsers.drop(offset).take(limit).list map {
          u => TinyUser(u.userID.get, u.firstName, u.lastName)
        }
      }
    }
  }
}
