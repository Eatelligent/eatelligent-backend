package repository.daos.slick

import repository.daos.TagDAO
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import repository.models.RecipeTag

import scala.concurrent.Future

class TagDAOSlick extends TagDAO {
  import play.api.Play.current


  def find(id: Long): Future[Option[RecipeTag]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickTags.filter(
          t => t.id === id
        ).firstOption match {
          case Some(tag) => Some(RecipeTag(tag.id, tag.name))
          case None => None
        }
      }
    }
  }

  def find(query: String): Future[Seq[RecipeTag]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickTags.filter(
          t => t.name like "%" + query + "%"
        )
        .list
        .map(t => RecipeTag(t.id, t.name))
      }
    }
  }

  def save(tag: RecipeTag): Future[RecipeTag] = {
    Future.successful {
      DB withSession { implicit session =>
        val dbTag = DBTag(tag.id, tag.name)
        slickTags.filter(_.id === dbTag.id).firstOption match {
          case Some(i) => slickTags.filter(_.id === dbTag.id).update(dbTag)
          case None => slickTags.insert(dbTag)
        }
        tag
      }
    }
  }

  def getAll: Future[Seq[RecipeTag]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickTags.list.map(t => RecipeTag(t.id, t.name))
      }
    }
  }
}
