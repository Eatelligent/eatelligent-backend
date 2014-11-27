package repository.models

import org.joda.time.DateTime

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[String],
                   description: String,
                   language: Int,
                   calories: Option[Double],
                   procedure: String,
                   spicy: Int,
                   created: Option[DateTime],
                   modified: Option[DateTime],
                   ingredients: Seq[IngredientForRecipe],
                   tags: Seq[String],
                   createdBy: Option[TinyUser]
                   )
