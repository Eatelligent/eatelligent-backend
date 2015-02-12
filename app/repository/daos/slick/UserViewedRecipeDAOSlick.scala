package repository.daos.slick

import org.joda.time.LocalDateTime
import repository.daos.UserViewedRecipeDAO
import repository.models.{TinyRecipe, UserViewedRecipe}
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._

import scala.concurrent.Future

class UserViewedRecipeDAOSlick extends UserViewedRecipeDAO {
  import play.api.Play.current

  def save(userId: Long, recipeId: Long, duration: Long): Future[Option[UserViewedRecipe]] = {
    Future.successful {
      val newInstance = DBUserViewedRecipe(userId, recipeId, duration, new LocalDateTime)
      DB withSession { implicit session =>
        slickUserViewedRecipes.filter(x => x.userId === userId && x.recipeId === recipeId).firstOption match {
          case Some(oldInstance) => slickUserViewedRecipes.filter(x => x.userId === userId && x.recipeId ===
            recipeId).update(newInstance)
          case None => slickUserViewedRecipes.insert(newInstance)
        }
      }
    }
    find(userId, recipeId)
  }

  def listAll(): Future[Seq[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        val join = for {
          (uvr, r) <- slickUserViewedRecipes innerJoin slickRecipes on(_.recipeId === _.id)
        } yield (uvr, r)
        join.buildColl[List]
          .map {
          case (uvr, r) => UserViewedRecipe(uvr.userId, TinyRecipe(r.id.get, r.name, r.image, r.description, r
            .spicy, r
            .time, r.difficulty), uvr.duration, uvr.lastSeen)
        }
      }
    }
  }

  def find(userId: Long, recipeId: Long): Future[Option[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        val join = for {
          (uvr, r) <- slickUserViewedRecipes innerJoin slickRecipes on(_.recipeId === _.id) if uvr.userId === userId && uvr.recipeId === recipeId
        } yield (uvr, r)
        val res = join.firstOption
          .map {
          case (uvr, r) => UserViewedRecipe(userId, TinyRecipe(r.id.get, r.name, r.image, r.description, r
            .spicy, r
            .time, r.difficulty), uvr.duration, uvr.lastSeen)
        }
        res.isEmpty match {
        case true => None
        case false => Some(res.head)
        }
      }
    }
  }

  def findRecipesUserHasViewed(userId: Long): Future[Seq[UserViewedRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        val join = for {
          (uvr, r) <- slickUserViewedRecipes innerJoin slickRecipes on(_.recipeId === _.id) if uvr.userId === userId
        } yield (uvr, r)
        join.buildColl[List]
        .map {
            case (uvr, r) => UserViewedRecipe(userId, TinyRecipe(r.id.get, r.name, r.image, r.description, r
              .spicy, r
              .time, r.difficulty), uvr.duration, uvr.lastSeen)
          }
      }
    }
  }

}
