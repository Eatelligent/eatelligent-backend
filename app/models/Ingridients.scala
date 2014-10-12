package models

import myUtils.WithMyDriver
import play.api.libs.json.JsValue

case class Ingredient(
                      id: Option[Long],
                      name: String,
                      image: Option[JsValue]
                       )
case class IngredientInRecipe(
                              recipeId: Long,
                              ingredientId: Long,
                              amount: Double
                               )
case class IngredientForRecipe(
                                ingredientId: Long,
                                recipeId: Long,
                                name: String,
                                image: Option[JsValue],
                                amount: Double
                                )

trait IngredientComponent extends WithMyDriver {
  import driver.simple._

  class Ingredients(tag: Tag) extends Table[Ingredient](tag, "ingredient") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")

    def * = (id, name, image) <> (Ingredient.tupled, Ingredient.unapply)
  }

  class IngredientsInRecipeTable(tag: Tag) extends Table[IngredientInRecipe](tag, "ingredient_in_recipe") {
    def recipeId = column[Long]("recipe_id")
    def ingredientId = column[Long]("ingredient_id")
    def amount = column[Double]("amount")
    def * = (recipeId, ingredientId, amount) <> (IngredientInRecipe.tupled, IngredientInRecipe.unapply)
  }

  val ingredients = TableQuery[Ingredients]

  val ingredientsInRecipe = TableQuery[IngredientsInRecipeTable]

  private val ingredientsAutoInc = ingredients returning ingredients.map(_.id) into {
    case (i, id) => i.copy(id = id)
  }

  def insert(ingredient: Ingredient)(implicit session: Session): Ingredient =
    ingredientsAutoInc.insert(ingredient)

  def findIngredientById(id: Long)(implicit session: Session): Option[Ingredient] = {
    ingredients.filter(_.id === id).list.headOption
  }

  def findIngredientsForRecipe(recipeId: Long)(implicit session: Session) : List[IngredientForRecipe]
  = {
    var list: List[IngredientForRecipe] = List()
    val join = for {
      (iir, i) <- ingredientsInRecipe innerJoin ingredients on (_.ingredientId === _.id) if iir.recipeId === recipeId
    } yield (i.id, iir.recipeId, i.name, i.image, iir.amount)

    val l: List[(Option[Long], Long, String, Option[JsValue], Double)] = join.buildColl[List]
    l.map{case (Some(iid), rid, name, image, amount) => IngredientForRecipe(iid, rid, name, image,
      amount)}
  }

}
