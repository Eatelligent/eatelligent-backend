package repository.models

import org.joda.time.DateTime

case class UserYesNoRateIngredient(
                                      userId: String,
                                      recipeId: Long,
                                      rating: Boolean,
                                      created: Option[DateTime]
                                      )
