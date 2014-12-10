package repository.models

import org.joda.time.LocalDateTime

case class UserYesNoRateIngredient(
                                      userId: Long,
                                      recipeId: Long,
                                      rating: Boolean,
                                      created: Option[LocalDateTime]
                                      )
