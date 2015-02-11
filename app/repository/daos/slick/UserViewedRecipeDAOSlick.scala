package repository.daos.slick

import org.joda.time.LocalDateTime
import repository.daos.UserViewedRecipeDAO
import repository.models.UserViewedRecipe
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._

import scala.concurrent.Future

class UserViewedRecipeDAOSlick extends UserViewedRecipeDAO {
  import play.api.Play.current

  def save(userId: Long, recipeId: Long, duration: Long): Future[UserViewedRecipe] = {
    Future.successful {
      val newInstance = DBUserViewedRecipe(userId, recipeId, duration, new LocalDateTime)
      DB withSession { implicit session =>
        slickUserViewedRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(oldInstance) => slickUserViewedRecipes.filter(x => x.userId === userId && x.recipeId ===
            recipeId).update(newInstance)
          case None => slickUserViewedRecipes.insert(newInstance)
        }
      }
      UserViewedRecipe(Some(userId), recipeId, duration, Some(newInstance.lastSeen))
    }
  }

  def listAll(): Future[Seq[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserViewedRecipes
          .list
          .map(x => UserViewedRecipe(Some(x.userId), x.recipeId, x.duration, Some(x.lastSeen)))
      }
    }
  }

  def find(userId: Long, recipeId: Long): Future[Option[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserViewedRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(found) => Some(UserViewedRecipe(Some(found.userId), found.recipeId, found.duration, Some(found
            .lastSeen)))
          case None => None
        }
      }
    }
  }

  def findRecipesUserHasViewed(userId: Long): Future[Seq[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserViewedRecipes.filter(_.userId === userId)
          .list
          .map(x => UserViewedRecipe(Some(x.userId), x.recipeId, x.duration, Some(x.lastSeen)))
      }
    }
  }

}
