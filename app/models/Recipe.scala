package models

import java.sql.Date
import java.util.Calendar

import myUtils.WithMyDriver
import play.api.libs.json.{JsValue, Json}
import org.joda.time.LocalDateTime

case class Recipe(
                  id: Option[Int],
                  name: String,
                  image: Option[JsValue],
                  description: String,
                  language: Int,
                  calories: Double,
                  procedure: String,
                  created: Date,
                  modified: Date
                  )

trait RecipeComponent extends WithMyDriver {
  import driver.simple._

  class Recipes(tag: Tag) extends Table[Recipe](tag, "recipe") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def image = column[Option[JsValue]]("image")
    def description = column[String]("description")
    def language = column[Int]("language")
    def calories = column[Double]("calories")
    def procedure = column[String]("procedure")
    def created = column[Date]("created")
    def modified = column[Date]("modified")

    def * = (id, name, image, description, language, calories, procedure, created, modified) <> (Recipe.tupled,
      Recipe.unapply)
  }

  val recipes = TableQuery[Recipes]

  private val recipesAutoInc = recipes returning recipes.map(_.id) into {
    case (r, id) => r.copy(id = id)
  }

  def insert(recipe: Recipe)(implicit session: Session): Recipe =
    recipesAutoInc.insert(recipe)


}
