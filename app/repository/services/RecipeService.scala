package repository.services

import repository.{TinyRecipe, Recipe}

import scala.concurrent.Future

trait RecipeService {

  def save(recipe: Recipe): Future[Recipe]

  def find(id: Long): Future[Option[Recipe]]

  def find(name: String): Future[List[TinyRecipe]]

  def getAll: Future[Seq[TinyRecipe]]

}
