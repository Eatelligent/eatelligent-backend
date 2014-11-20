package repository.services

import repository.RecipeTag

import scala.concurrent.Future

trait TagService {

  def save(tag: RecipeTag): Future[RecipeTag]

  def getAll(): Future[Seq[RecipeTag]]

}
