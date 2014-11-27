package repository.services

import com.google.inject.Inject
import repository.daos.LanguageDAO
import repository.models.Language

import scala.concurrent.Future

class LanguageServiceImpl @Inject() (languageDAO: LanguageDAO) extends LanguageService {

  def find(id: Long): Future[Option[Language]] = languageDAO.find(id)

  def save(language: Language): Future[Option[Language]] = languageDAO.save(language)

  def getAll: Future[Seq[Language]] = languageDAO.getAll


}
