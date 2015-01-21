package repository.models

import org.joda.time.LocalDateTime

case class UserStarRateRecipe(
                                 userId: Option[Long],
                                 recipeId: Long,
                                 stars: Double,
                                 created: Option[LocalDateTime]
                                 )
