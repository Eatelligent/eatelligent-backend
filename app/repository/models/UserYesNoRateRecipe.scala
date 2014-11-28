package repository.models

case class UserYesNoRateRecipe(
                                  userId: String,
                                  recipeId: Long,
                                  rating: Boolean
                                  )
