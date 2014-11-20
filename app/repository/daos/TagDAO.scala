package repository.daos

import repository.RecipeTag

import scala.concurrent.Future

trait TagDAO {

  def find(id: Long): Future[Option[RecipeTag]]

  def save(tag: RecipeTag): Future[RecipeTag]

  def getAll(): Future[Seq[RecipeTag]]

}
