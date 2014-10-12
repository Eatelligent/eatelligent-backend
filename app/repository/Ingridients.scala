package models

import myUtils.WithMyDriver
import play.api.libs.json.JsValue

case class IngredientSchema(
                      id: Option[Long],
                      name: String,
                      image: Option[JsValue]
                       )
case class IngredientInRecipeSchema(
                              recipeId: Long,
                              ingredientId: Long,
                              amount: Double
                               )
case class IngredientForRecipe(
                                ingredientId: Option[Long],
                                recipeId: Option[Long],
                                name: String,
                                image: Option[JsValue],
                                amount: Double
                                )

trait IngredientComponent extends WithMyDriver {
  import driver.simple._

  class Ingredients(tag: Tag) extends Table[IngredientSchema](tag, "ingredient") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")

    def * = (id, name, image) <> (IngredientSchema.tupled, IngredientSchema.unapply)
  }

  class IngredientsInRecipeTable(tag: Tag) extends Table[IngredientInRecipeSchema](tag, "ingredient_in_recipe") {
    def recipeId = column[Long]("recipe_id")
    def ingredientId = column[Long]("ingredient_id")
    def amount = column[Double]("amount")
    def * = (recipeId, ingredientId, amount) <> (IngredientInRecipeSchema.tupled, IngredientInRecipeSchema.unapply)
  }

  val ingredients = TableQuery[Ingredients]

  val ingredientsInRecipe = TableQuery[IngredientsInRecipeTable]

  private val ingredientsAutoInc = ingredients returning ingredients.map(_.id) into {
    case (i, id) => i.copy(id = id)
  }

//  def insert(ingredient: IngredientSchema)(implicit session: Session): IngredientSchema =
//    ingredientsAutoInc.insert(ingredient)

  def insertIngredient(ingredient: IngredientSchema)(implicit session: Session): Long = {
    (ingredients returning ingredients.map(_.id) += ingredient).toList.head
  }

  def findIngredientById(id: Long)(implicit session: Session): Option[IngredientSchema] = {
    ingredients.filter(_.id === id).list.headOption
  }

  def findIngredientsForRecipe(recipeId: Long)(implicit session: Session) : Seq[IngredientForRecipe]
  = {
    var list: Seq[IngredientForRecipe] = List()
    val join = for {
      (iir, i) <- ingredientsInRecipe innerJoin ingredients on (_.ingredientId === _.id) if iir.recipeId === recipeId
    } yield (iir.ingredientId, iir.recipeId, i.name, i.image, iir.amount)

    val l: Seq[(Long, Long, String, Option[JsValue], Double)] = join.buildColl[List]
    l.map{
      case (iid, rid, name, image, amount) => IngredientForRecipe(Some(iid), Some(rid), name, image, amount)
    }
  }

}
