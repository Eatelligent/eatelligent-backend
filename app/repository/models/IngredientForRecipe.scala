package repository.models

import play.api.libs.json.JsValue

case class IngredientForRecipe(
                                ingredientId: Option[Long],
                                name: String,
                                image: Option[JsValue],
                                amount: Double
                                )
