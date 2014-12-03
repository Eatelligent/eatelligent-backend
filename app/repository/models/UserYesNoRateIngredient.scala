package repository.models

import org.joda.time.LocalDateTime

case class UserYesNoRateIngredient(
                                      userId: String,
                                      recipeId: Long,
                                      rating: Boolean,
                                      created: Option[LocalDateTime]
                                      )
