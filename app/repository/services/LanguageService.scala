package repository.services

import repository.models.Language

import scala.concurrent.Future

trait LanguageService {

  def find(id: Long): Future[Option[Language]]

  def save(language: Language): Future[Option[Language]]

  def getAll: Future[Seq[Language]]

}
