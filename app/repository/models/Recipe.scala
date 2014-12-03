package repository.models

import org.joda.time.LocalDateTime

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[String],
                   description: String,
                   language: Int,
                   calories: Option[Double],
                   procedure: String,
                   spicy: Int,
                   time: Int,
                   created: Option[LocalDateTime],
                   modified: Option[LocalDateTime],
                   published: Option[LocalDateTime],
                   deleted: Option[LocalDateTime],
                   ingredients: Seq[IngredientForRecipe],
                   tags: Seq[String],
                   createdBy: Option[TinyUser]
                   )
