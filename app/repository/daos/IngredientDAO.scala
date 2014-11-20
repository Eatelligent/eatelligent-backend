package repository.daos

import repository.Ingredient

import scala.concurrent.Future

trait IngredientDAO {

  def save(ingredient: Ingredient): Future[Ingredient]

  def find(id: Long): Future[Option[Ingredient]]

  def find(name: String): Future[Option[Ingredient]]

  def getAll: Future[Seq[Ingredient]]

}
