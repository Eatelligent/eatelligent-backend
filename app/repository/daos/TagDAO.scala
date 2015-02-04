package repository.daos

import repository.models.RecipeTag

import scala.concurrent.Future

trait TagDAO {

  def find(id: Long): Future[Option[RecipeTag]]

  def find(query: String): Future[Seq[RecipeTag]]

  def save(tag: RecipeTag): Future[RecipeTag]

  def getAll: Future[Seq[RecipeTag]]

}
