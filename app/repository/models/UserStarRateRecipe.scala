package repository.models

import org.joda.time.LocalDateTime

case class UserStarRateRecipe(
                                 userId: String,
                                 recipeId: Long,
                                 stars: Double,
                                 created: Option[LocalDateTime]
                                 )
