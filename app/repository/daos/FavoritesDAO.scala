package repository.daos

import repository.models.{TinyRecipe, Favorite}

import scala.concurrent.Future

trait FavoritesDAO {

  def addToFavorites(userId: Long, recipeId: Long): Future[Favorite]

  def getFavoritesForUser(userId: Long): Future[Seq[TinyRecipe]]

  def removeFromFavorites(userId: Long, recipeId: Long)

}
