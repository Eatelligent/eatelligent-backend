package repository.daos

import repository.models.UserYesNoRecipe

import scala.concurrent.Future

trait UserYesNoRecipeDAO {

  def save(userId: Long, recipeId: Long, yesNo: Int): Future[Option[UserYesNoRecipe]]

  def find(userId: Long, recipeId: Long): Future[Option[UserYesNoRecipe]]

}
