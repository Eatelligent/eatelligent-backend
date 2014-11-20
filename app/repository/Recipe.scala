package repository

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
                   tags: Seq[RecipeTag],
                   createdBy: TinyUser
                   )

case class TinyRecipe(
                       id: Long,
                       name: String,
                       image: Option[String]
                       )
