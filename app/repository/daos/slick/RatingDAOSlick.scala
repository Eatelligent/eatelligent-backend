package repository.daos.slick

import org.joda.time.{DateTime, LocalDateTime}
import repository.exceptions.NoSuchRecipeException
import repository.daos.RatingDAO
import repository.models.{UserYesNoRateIngredient, UserYesNoRateRecipe, UserStarRateRecipe}
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import play.api.Play.current
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import scala.concurrent.Future

class RatingDAOSlick extends RatingDAO {

  def saveUserStarRateRecipe(rating: UserStarRateRecipe): Future[Option[UserStarRateRecipe]] = {
    DB withSession { implicit session =>
      val recipeExists = DB withSession { implicit session =>
        slickRecipes.filter(_.id === rating.recipeId).length.run > 0
      }
      if (!recipeExists)
        throw new NoSuchRecipeException("No recipe with id: " + rating.recipeId + " in the databse.")

      // Looking up if this recipe has been recommended
      val given = slickGivenRecommendations
        .filter(x => x.userId === rating.userId && x.recipeId === rating.recipeId)
        .sortBy(_.created.desc).take(1).firstOption
          match {
            case Some(g) => (g.from, g.data)
            case None => ("search", None)
          }


      slickUserStarRateRecipes.filter(x => x.userId === rating.userId && x.recipeId === rating.recipeId)
        .firstOption match {
        // Update old rating
        case Some(x) => slickUserStarRateRecipes.filter(x => x.userId === rating.userId && x.recipeId === rating.recipeId)
          .update(DBUserStarRateRecipe(rating.userId.get, rating.recipeId, rating
          .stars, new LocalDateTime(), new DateTime().getMillis, given._1, given._2))
        // Insert new rating
        case None => slickUserStarRateRecipes.insert(DBUserStarRateRecipe(rating.userId.get, rating.recipeId, rating
          .stars, new LocalDateTime(), new DateTime().getMillis, given._1, given._2))
      }
      findUserStarRateRecipe(rating.userId.get, rating.recipeId)
    }
  }

  def findUserStarRateRecipe(userId: Long, recipeId: Long): Future[Option[UserStarRateRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserStarRateRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(x) => Some(UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created)))
          case None => None
        }
      }
    }
  }

  def findStarRatingsForRecipe(recipeId: Long): Future[Seq[UserStarRateRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserStarRateRecipes.filter(_.recipeId === recipeId).list map {
          x => UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created))
        }
      }
    }
  }

  def findStarRatingsForUser(userId: Long): Future[Seq[UserStarRateRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserStarRateRecipes.filter(_.userId === userId).list map {
          x => UserStarRateRecipe(Some(x.userId), x.recipeId, x.stars, Some(x.created))
        }
      }
    }
  }

  def getAverageRatingForRecipe(recipeId: Long): Future[Option[Double]] = {
    Future.successful {
      DB withSession { implicit session =>
        Q.queryNA[Option[Double]]("SELECT avg(rating) AS avg_rating " +
          "FROM user_star_rate_recipe " +
          "WHERE recipe_id = '" + recipeId + "';").first
      }
    }
  }

  // TODO remove this
  def saveUserYesNoRateRecipe(rating: UserYesNoRateRecipe): Future[Option[UserYesNoRateRecipe]] = {
    Future.successful(None)
  }
  // TODO remove this
  def saveUserYesNoRateIngredient(rating: UserYesNoRateIngredient): Future[Option[UserYesNoRateIngredient]] = {
    Future.successful(None)
  }

  def getMaxRecipesRates(userId: Long, limit: Int): Seq[(Long, Double)] = {
    DB withSession { implicit session =>
      slickUserStarRateRecipes.filter(_.userId === userId).sortBy(_.rating).take(limit).map(x => (x.recipeId, x
        .rating)).list
    }
  }

}
