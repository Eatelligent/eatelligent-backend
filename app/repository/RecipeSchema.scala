package models

import java.sql.Date

import myUtils.WithMyDriver
import play.api.libs.json.{JsValue, Json}

case class RecipeSchema(
                  id: Option[Long],
                  name: String,
                  image: Option[String],
                  description: String,
                  language: Int,
                  calories: Double,
                  procedure: String,
                  created: Date,
                  modified: Date
                  )

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[String],
                   description: String,
                   language: Int,
                   calories: Double,
                   procedure: String,
                   created: Date,
                   modified: Date,
                   ingredients: Seq[IngredientForRecipe]
                   )

case class TinyRecipe(
                      id: Long,
                      name: String,
                      image: Option[String]
                       )

trait RecipeComponent extends WithMyDriver with IngredientComponent {
  import driver.simple._

  class Recipes(tag: Tag) extends Table[RecipeSchema](tag, "recipe") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[String]]("image")
    def description = column[String]("description")
    def language = column[Int]("language")
    def calories = column[Double]("calories")
    def procedure = column[String]("procedure")
    def created = column[Date]("created")
    def modified = column[Date]("modified")

    def * = (id, name, image, description, language, calories, procedure, created, modified) <> (RecipeSchema.tupled,
      RecipeSchema.unapply)
  }

  val recipes = TableQuery[Recipes]

  private val recipesAutoInc = recipes returning recipes.map(_.id) into {
    case (r, id) => r.copy(id = id)
  }

  def ins(recipe: RecipeSchema)(implicit session: Session): Long = {
    (recipes returning recipes.map(_.id) += recipe).toList.head
  }

  def insert(recipe: RecipeSchema)(implicit session: Session): RecipeSchema =
    recipesAutoInc.insert(recipe)

  def saveRecipeToDb(r: Recipe)(implicit session: Session): Recipe = {
    session.withTransaction{
      val rid = ins(RecipeSchema(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
        r.created, r.modified))
      r.ingredients.foreach{
        i =>
          if (i.ingredientId == None) {
            val iid: Long = insertIngredient(IngredientSchema(None, i.name, i.image))
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, iid, i.amount))
          } else {
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, i.ingredientId.toList.head, i.amount))
          }
      }
    }
    r
  }

  def findRecipeById(id: Long)(implicit session: Session): Option[Recipe] = {
    val res = Option(recipes.filter(_.id === id).list)
    res match {
      case Some(r) =>
        if (r.isEmpty) {
          None
        }
        else
          Some(transformRecipes(r).head)
      case None => None
    }
  }

  def findRecipesByName(q: String)(implicit sessions: Session): Option[List[TinyRecipe]] = {
    val res = Option(recipes.filter(_.name.toLowerCase like "%" + q.toLowerCase + "%").list)
    res match {
      case Some(r) => mapToTinyRecipe(Some(transformRecipes(r)))
    }
  }

  def mapToTinyRecipe(recipes: Option[List[Recipe]]): Option[List[TinyRecipe]] = {
    recipes match {
      case Some(r) => Option(r.map{
        x =>
        TinyRecipe(x.id.toList.head, x.name, x.image)
      })
      case None => None
    }
  }

  def transformRecipes(rawRecipes: List[RecipeSchema])(implicit sessions: Session): List[Recipe] = {
    rawRecipes.map{
      r =>
        val i = findIngredientsForRecipe(r.id.last)
        Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
            r.created, r.modified, i)
    }
  }

  def updateImage(recipeId: Long, image: String)(implicit sessions: Session) {
    val q = for { r <- recipes if r.id === recipeId } yield r.image

    val a = q.run
//    if (a.head.nonEmpty)
//      deleteImage
    q.update(Some(image))
  }

//  def deleteImage = ???

}
