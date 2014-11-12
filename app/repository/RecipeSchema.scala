package repository

import java.sql.Date

import myUtils.WithMyDriver
import org.joda.time.DateTime
import play.api.libs.json.{JsValue, Json}

case class RecipeSchema(
                  id: Option[Long],
                  name: String,
                  image: Option[String],
                  description: String,
                  language: Int,
                  calories: Double,
                  procedure: String,
                  created: DateTime,
                  modified: DateTime,
                  createdById: Long
                  )

case class Recipe(
                   id: Option[Long],
                   name: String,
                   image: Option[String],
                   description: String,
                   language: Int,
                   calories: Option[Double],
                   procedure: String,
                   created: Option[DateTime],
                   modified: Option[DateTime],
                   ingredients: Seq[IngredientForRecipe],
                   tags: Seq[TagSchema],
                   createdBy: TinyUser
                   )

case class TinyRecipe(
                      id: Long,
                      name: String,
                      image: Option[String]
                       )

trait RecipeComponent extends WithMyDriver with IngredientComponent with TagComponent with UserComponent {
  import driver.simple._

  class Recipes(tag: Tag) extends Table[RecipeSchema](tag, "recipe") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[String]]("image")
    def description = column[String]("description")
    def language = column[Int]("language")
    def calories = column[Double]("calories")
    def procedure = column[String]("procedure")
    def created = column[DateTime]("created")
    def modified = column[DateTime]("modified")
    def createdById = column[Long]("created_by")

    def * = (id, name, image, description, language, calories, procedure, created, modified,
      createdById) <> (RecipeSchema.tupled,
      RecipeSchema.unapply)
  }

  def recipes = TableQuery[Recipes]

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
        new DateTime(), new DateTime(), r.createdBy.id))
      r.ingredients.foreach{
        i =>
          if (i.ingredientId == None) {
            val iid: Long = insertIngredient(IngredientSchema(None, i.name, i.image))
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, iid, i.amount))
          } else {
            ingredientsInRecipe.insert(IngredientInRecipeSchema(rid, i.ingredientId.toList.head, i.amount))
          }
      }
      r.tags.foreach{
        t =>
          println("TAGGER: " + t)
          if (t.id == None) {
            val tagId = insertTag(t)
            println("NYID: " + tagId)
            tagsInRecipes.insert(TagForRecipe(rid, tagId))
          } else {
            tagsInRecipes.insert(TagForRecipe(rid, t.id.get))
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
      case None => None
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
        val t = findTagsForRecipe(r.id.last)
        val u = findUserById(r.createdById).map(u => TinyUser(u.id.get, u.name))

<<<<<<< HEAD
        val rec = Recipe(r.id, r.name, r.image, r.description, r.language, Some(r.calories), r.procedure,
=======
        val rec = Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
>>>>>>> made date fields optional and saving new tags
            Some(r.created), Some(r.modified), i, t, u.get)
        rec
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
