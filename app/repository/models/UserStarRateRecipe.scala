package repository.models

import org.joda.time.DateTime

case class UserStarRateRecipe(
                                 userId: String,
                                 recipeId: Long,
                                 stars: Double,
                                 created: Option[DateTime]
                                 )
