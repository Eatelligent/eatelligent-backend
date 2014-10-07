package models

import myUtils.WithMyDriver
import play.api.libs.json.JsValue

case class Ingredient(
                      id: Option[Long],
                      name: String,
                      image: Option[JsValue]
                       )

trait IngredientComponent extends WithMyDriver {
  import driver.simple._

  class Ingredients(tag: Tag) extends Table[Ingredient](tag, "ingredient") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")

    def * = (id, name, image) <> (Ingredient.tupled, Ingredient.unapply)
  }

  val ingredients = TableQuery[Ingredients]

  private val ingredientsAutoInc = ingredients returning ingredients.map(_.id) into {
    case (i, id) => i.copy(id = id)
  }

  def insert(ingredient: Ingredient)(implicit session: Session): Ingredient =
    ingredientsAutoInc.insert(ingredient)

  def findIngredientById(id: Long)(implicit session: Session): Option[Ingredient] = {
    ingredients.filter(_.id === id).list.headOption
  }
}
