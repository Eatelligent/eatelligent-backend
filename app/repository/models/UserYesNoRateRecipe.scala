package repository.models

import org.joda.time.DateTime

case class UserYesNoRateRecipe(
                                  userId: String,
                                  recipeId: Long,
                                  rating: Boolean,
                                  created: Option[DateTime]
                                  )
