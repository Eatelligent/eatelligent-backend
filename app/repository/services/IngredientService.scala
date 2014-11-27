package repository.services

import repository.models.Ingredient

import scala.concurrent.Future

trait IngredientService {

  def save(ingredient: Ingredient): Future[Ingredient]

  def find(id: Long): Future[Option[Ingredient]]

  def find(name: String): Future[Option[Ingredient]]

  def getAll: Future[Seq[Ingredient]]

}
