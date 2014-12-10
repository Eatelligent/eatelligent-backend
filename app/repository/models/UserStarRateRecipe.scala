package repository.models

import org.joda.time.LocalDateTime

case class UserStarRateRecipe(
                                 userId: Long,
                                 recipeId: Long,
                                 stars: Double,
                                 created: Option[LocalDateTime]
                                 )
