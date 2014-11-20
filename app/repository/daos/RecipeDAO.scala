package repository.daos

import repository.{TinyRecipe, Recipe}

import scala.concurrent.Future

trait RecipeDAO {

  def find(id: Long): Future[Option[Recipe]]

  def find(name: String): Future[List[TinyRecipe]]

  def save(recipe: Recipe): Future[Recipe]

  def getAll: Future[Seq[TinyRecipe]]

}
