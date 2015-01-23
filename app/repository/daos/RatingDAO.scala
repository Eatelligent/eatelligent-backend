package repository.daos

import repository.models.{UserYesNoRateIngredient, UserYesNoRateRecipe, UserStarRateRecipe}

import scala.concurrent.Future

trait RatingDAO {

  def saveUserStarRateRecipe(rating: UserStarRateRecipe): Future[Option[UserStarRateRecipe]]
  def saveUserYesNoRateRecipe(rating: UserYesNoRateRecipe): Future[Option[UserYesNoRateRecipe]]
  def saveUserYesNoRateIngredient(rating: UserYesNoRateIngredient): Future[Option[UserYesNoRateIngredient]]

  def findStarRatingsForRecipe(recipeId: Long): Future[Seq[UserStarRateRecipe]]

  def findStarRatingsForUser(userId: Long): Future[Seq[UserStarRateRecipe]]

  def findUserStarRateRecipe(userId: Long, recipeId: Long): Future[Option[UserStarRateRecipe]]

  def getAverageRatingForRecipe(recipeId: Long): Future[Option[Double]]

}
