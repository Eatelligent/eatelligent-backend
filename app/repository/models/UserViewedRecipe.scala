package repository.models

import org.joda.time.LocalDateTime

case class UserViewedRecipe(
                               userId: Long,
                               recipe: TinyRecipe,
                               duration: Long,
                               lastSeen: LocalDateTime
                               )

case class UserViewedRecipePost(
                             recipeId: Long,
                             duration: Long
                             )
