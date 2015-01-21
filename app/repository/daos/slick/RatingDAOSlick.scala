package repository.daos.slick

import org.joda.time.{DateTime, LocalDateTime}
import repository.Exceptions.NoSuchRecipeException
import repository.daos.RatingDAO
import repository.models.{UserYesNoRateIngredient, UserYesNoRateRecipe, UserStarRateRecipe}
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import play.api.Play.current

import scala.concurrent.Future

class RatingDAOSlick extends RatingDAO {

  def saveUserStarRateRecipe(rating: UserStarRateRecipe): Future[Option[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      val recipeExists = DB withSession { implicit session =>
        slickRecipes.filter(_.id === rating.recipeId).length.run > 0
      }
      if (!recipeExists)
        throw new NoSuchRecipeException("No recipe with id: " + rating.recipeId + " in the databse.")
      slickUserStarRateRecipes.filter(x => x.userId === rating.userId && x.recipeId === rating.recipeId).firstOption match {
        case Some(x) => slickUserStarRateRecipes.filter(x => x.userId === rating.userId && x.recipeId === rating.recipeId)
          .update(DBUserStarRateRecipe(rating.userId.get, rating.recipeId, rating
          .stars, new LocalDateTime(), new DateTime().getMillis))
        case None => slickUserStarRateRecipes.insert(DBUserStarRateRecipe(rating.userId.get, rating.recipeId, rating
          .stars, new LocalDateTime(), new DateTime().getMillis))
      }
      findUserStarRateRecipe(rating.userId.get, rating.recipeId)
    }
  }

  def findUserStarRateRecipe(userId: Long, recipeId: Long): Future[Option[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(x) => Some(UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created)))
          case None => None
        }
      }
    }
  }

  def findStarRatingsForRecipe(recipeId: Long): Future[Seq[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(_.recipeId === recipeId).list map {
          x => UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created))
        }
      }
    }
  }

  def findStarRatingsForUser(userId: Long): Future[Seq[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(_.userId === userId).list map {
          x => UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created))
        }
      }
    }
  }

  def saveUserYesNoRateRecipe(rating: UserYesNoRateRecipe): Future[Option[UserYesNoRateRecipe]] = {
    Future.successful(None)
  }

  def saveUserYesNoRateIngredient(rating: UserYesNoRateIngredient): Future[Option[UserYesNoRateIngredient]] = {
    Future.successful(None)
  }

}
