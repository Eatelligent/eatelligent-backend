package repository.models

import org.joda.time.LocalDateTime

case class UserViewedRecipe(
                               userId: Option[Long],
                               recipeId: Long,
                               duration: Long,
                               lastSeen: Option[LocalDateTime]
                               )
