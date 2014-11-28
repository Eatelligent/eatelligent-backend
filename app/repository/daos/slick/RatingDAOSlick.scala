package repository.daos.slick

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
      slickUserStarRateRecipes.insert(DBUserStarRateRecipe(rating.userId, rating.recipeId, rating.stars))
      findUserStarRateRecipe(rating.userId, rating.recipeId)
    }
  }

  def findUserStarRateRecipe(userId: String, recipeId: Long): Future[Option[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(x) => Some(UserStarRateRecipe(x.userId, x.recipeId, x.stars))
          case None => None
        }
      }
    }
  }

  def findStarRatingsForRecipe(recipeId: Long): Future[Seq[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(_.recipeId === recipeId).list map {
          x => UserStarRateRecipe(x.userId, x.recipeId, x.stars)
        }
      }
    }
  }

  def findStarRatingsForUser(userId: String): Future[Seq[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickUserStarRateRecipes.filter(_.userId === userId).list map {
          x => UserStarRateRecipe(x.userId, x.recipeId, x.stars)
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
