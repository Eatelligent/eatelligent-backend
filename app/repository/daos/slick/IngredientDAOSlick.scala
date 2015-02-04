package repository.daos.slick

import repository.exceptions.{NoSuchIngredientException, DuplicateException}
import repository.daos.IngredientDAO
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import repository.models.{IngredientTag, Ingredient}
import play.api.db.slick._

import scala.concurrent.Future

class IngredientDAOSlick extends IngredientDAO {
  import play.api.Play.current

  def getAll: Future[Seq[Ingredient]] = {
    Future.successful{
      DB withSession { implicit session =>
        slickIngredients.list.map(i => Ingredient(i.id, i.name, i.image, findTagsForIngredient(i.id.get)))
      }
    }
  }

  def find(id: Long): Future[Option[Ingredient]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickIngredients.filter(
          i => i.id === id
        ).firstOption match {
          case Some(ingredient) =>
            val tags = findTagsForIngredient(id)
            Some(Ingredient(ingredient.id, ingredient.name, ingredient.image, tags))
          case None => None
        }
      }
    }
  }

  def find(name: String): Future[Option[Ingredient]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickIngredients.filter(
          _.name.toLowerCase like "%" + name.toLowerCase + "%"
        ).firstOption match {
          case Some(ingredient) =>
            val tags = findTagsForIngredient(ingredient.id.get)
            Some(Ingredient(ingredient.id, ingredient.name, ingredient.image, tags))
          case None => None
        }
      }
    }
  }

  def findTagsForIngredient(ingredientId: Long): Seq[String] = {
    DB withSession { implicit session =>
      (for {
        (tfi, t) <- slickIngredientInTags innerJoin slickIngredientTags on (_.tagId === _.id) if tfi.ingredientId ===
        ingredientId
      } yield t.name)
      .run
    }
  }

  def save(ingredient: Ingredient): Future[Option[Ingredient]] = {
    val id = DB withTransaction { implicit session =>
      val dbIngredient = DBIngredient(None, ingredient.name, ingredient.image)
      val ingredientId: Long = slickIngredients.filter(_.name.toLowerCase === dbIngredient.name.toLowerCase)
        .firstOption
      match {
        case Some(i) => throw new DuplicateException("Ingredient already exists")
        case None => insertIngredient(dbIngredient)
      }
      ingredient.tags.foreach {
        t =>
          val tagId = saveTag(t).id.get
          slickIngredientInTags.insert(DBIngredientInTag(ingredientId, tagId))
      }
      ingredientId
    }
    find(id)
  }

  def update(ingredient: Ingredient, ingredientId: Long): Future[Option[Ingredient]] = {
    DB withTransaction  { implicit session =>
      val dbIngredient = DBIngredient(Some(ingredientId), ingredient.name, ingredient.image)
      slickIngredients.filter(_.id === ingredientId)
        .firstOption
      match {
        case Some(i) => slickIngredients.filter(_.id === ingredientId).update(dbIngredient)
        case None => throw new NoSuchIngredientException(ingredientId)
      }
      slickIngredientInTags.filter(_.ingredientId === ingredientId).delete
      ingredient.tags.foreach {
        t =>
          val tagId = saveTag(t).id.get
          slickIngredientInTags.insert(DBIngredientInTag(ingredientId, tagId))
      }
    }
    find(ingredientId)
  }

  def saveTag(name: String): DBIngredientTag = {
    DB withSession { implicit session =>
      val tags = slickIngredientTags.filter(_.name.toLowerCase === name.toLowerCase).firstOption
      if (tags.isEmpty) {
        val tid = insertIngredientTag(DBIngredientTag(None, name))
        DBIngredientTag(Some(tid), name)
      }
      else {
        DBIngredientTag(tags.head.id, name)
      }
    }
  }

  def getAllIngredientTags: Future[List[IngredientTag]] = {
    Future.successful {
      DB withSession { implicit session =>
        slickIngredientTags.list.map(i => IngredientTag(i.id, i.name))
      }
    }
  }
}
