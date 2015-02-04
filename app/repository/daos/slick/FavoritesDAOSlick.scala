package repository.daos.slick

import org.postgresql.util.PSQLException
import repository.Exceptions.{NoSuchRecipeException, DuplicateException, NoSuchFavoriteFoundException}
import repository.daos.FavoritesDAO
import repository.models.Favorite
import com.google.inject.Inject
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import repository.models._
import scala.concurrent._
import play.api.Play.current

class FavoritesDAOSlick @Inject() extends FavoritesDAO {


  def addToFavorites(userId: Long, recipeId: Long): Future[Favorite] = {
    Future.successful {
      DB withSession { implicit session =>
        try {
          slickRecipes.filter(_.id === recipeId).firstOption match {
            case Some(r) => slickFavorites.insert(DBFavorites(userId, recipeId))
            case None => throw new NoSuchRecipeException(recipeId)
          }
        }
        catch {
          case (e: PSQLException) => throw new DuplicateException()
        }
        Favorite(userId, recipeId)
      }
    }
  }

  def getFavoritesForUser(userId: Long): Future[Seq[TinyRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        val join = for {
          (f, r) <- slickFavorites innerJoin slickRecipes on (_.recipeId === _.id) if f.userId === userId
        } yield(f.recipeId, r.name, r.image)
        join.buildColl[List].map {
          case (id, name, image) => TinyRecipe(id, name, image)
        }
      }
    }
  }

  def removeFromFavorites(userId: Long, recipeId: Long) = {
    Future.successful {
      DB withSession { implicit session =>
        slickFavorites.filter(f => f.recipeId === recipeId && f.userId === userId).firstOption match {
          case Some(fav) =>
            slickFavorites.filter(f => f.recipeId === recipeId && f.userId === userId).delete
          case None => throw new NoSuchFavoriteFoundException(userId, recipeId)
        }
      }
    }
  }

}
