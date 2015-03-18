package repository.daos.slick

import org.joda.time.LocalDateTime
import myUtils.MyPostgresDriver.simple._
import repository.daos.UserYesNoRecipeDAO
import repository.daos.slick.DBTableDefinitions._
import play.api.db.slick._
import repository.models.UserYesNoRecipe

import scala.concurrent.Future

class UserYesNoRecipeDAOSlick extends UserYesNoRecipeDAO {
  import play.api.Play.current

  def save(userId: Long, recipeId: Long, yesNo: Int): Future[Option[UserYesNoRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserYesNoRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(found) => slickUserYesNoRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId)
            .update(DBUserYesNoRecipe(userId, recipeId, found.rating + yesNo, new LocalDateTime()))
          case None => slickUserYesNoRateRecipes.insert(DBUserYesNoRecipe(userId, recipeId, yesNo, new LocalDateTime()))
        }
      }
    }
    find(userId, recipeId)
  }



  def find(userId: Long, recipeId: Long): Future[Option[UserYesNoRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserYesNoRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId)
          .firstOption match {
          case Some(u) => Some(UserYesNoRecipe(Some(u.userId), u.recipeId, u.rating, u.lastSeen))
          case None => None
        }
      }
    }
  }
}
