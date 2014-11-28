package repository.models

case class UserYesNoRateIngredient(
                                      userId: String,
                                      recipeId: Long,
                                      rating: Boolean
                                      )
