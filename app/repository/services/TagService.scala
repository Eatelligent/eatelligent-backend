package repository.services

import repository.models.RecipeTag

import scala.concurrent.Future

trait TagService {

  def save(tag: RecipeTag): Future[RecipeTag]

  def getAll: Future[Seq[RecipeTag]]

}
