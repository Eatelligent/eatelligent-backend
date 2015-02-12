package repository.models

import org.joda.time.LocalDateTime

case class UserYesNoRecipe(
                              userId: Option[Long],
                              recipeId: Long,
                              rating: Int,
                              lastSeen: LocalDateTime
                              )
