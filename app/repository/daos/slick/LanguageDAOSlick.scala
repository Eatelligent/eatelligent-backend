package repository.daos.slick

import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import models.daos.slick.DBTableDefinitions._
import repository.daos.LanguageDAO
import repository.models.Language

import scala.concurrent.Future

class LanguageDAOSlick extends LanguageDAO {
  import play.api.Play.current

  def find(id: Long): Future[Option[Language]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickLanguages.filter (
          _.id === id
        ).firstOption match {
          case Some(lang) => Some(Language(lang.id, lang.name, lang.locale))
          case None => None
        }
      }
    }
  }

  def save(language: Language): Future[Option[Language]] = {
    val id = DB withSession { implicit session =>
      val dbLang = DBLanguage(language.id, language.name, language.locale)
      Some(insertLanguage(dbLang))
    }
    find(id.get)
  }

  def getAll: Future[Seq[Language]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickLanguages.list.map(l => Language(l.id, l.name, l.locale))
      }
    }
  }

}
