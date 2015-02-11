package repository.daos

import repository.models.UserViewedRecipe

import scala.concurrent.Future

trait UserViewedRecipeDAO {

  def save(userId: Long, recipeId: Long, duration: Long): Future[UserViewedRecipe]

  def listAll(): Future[Seq[UserViewedRecipe]]

  def find(userId: Long, recipeId: Long): Future[Option[UserViewedRecipe]]

  def findRecipesUserHasViewed(userId: Long): Future[Seq[UserViewedRecipe]]

}
