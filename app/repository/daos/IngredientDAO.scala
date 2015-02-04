package repository.daos

import repository.models.{IngredientTag, Ingredient}

import scala.concurrent.Future

trait IngredientDAO {

  def save(ingredient: Ingredient): Future[Option[Ingredient]]

  def find(id: Long): Future[Option[Ingredient]]

  def find(name: String): Future[Option[Ingredient]]

  def getAll: Future[Seq[Ingredient]]

  def update(ingredient: Ingredient, ingredientId: Long): Future[Option[Ingredient]]

  def getAllIngredientTags: Future[Seq[IngredientTag]]

}
