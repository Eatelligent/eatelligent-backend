package repository.daos.slick

import repository.exceptions.{NoSuchIngredientException, DuplicateException}
import repository.daos.IngredientDAO
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import repository.models.{IngredientTag, Ingredient}
import play.api.db.slick.DB
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import scala.concurrent.Future
import play.api.Play.current


class IngredientDAOSlick extends IngredientDAO {

  implicit val getIngredientResult = GetResult(i => Ingredient(Some(i.nextLong()), i.nextString(), i.nextStringArray()))

  def getAll: Future[Seq[Ingredient]] = {
    Future.successful{
      DB withSession { implicit session =>
        Q.queryNA[Ingredient](
          "SELECT i.id, i.name, array_agg(it.name) " +
            "FROM ingredient as i " +
            "LEFT OUTER JOIN ingredient_in_tag as iit " +
            "ON (i.id = iit.ingredient_id) " +
            "LEFT OUTER JOIN ingredient_tag as it " +
            "ON (iit.tag_id = it.id) " +
            "GROUP BY i.id, i.name " +
            "ORDER BY i.id DESC;"
        )
        .list
      }
    }
  }

  def find(id: Long): Future[Option[Ingredient]] = {
    Future.successful {
      DB withSession { implicit session =>
        Q.queryNA[Ingredient](
          "SELECT i.id, i.name, array_agg(it.name) " +
            "FROM ingredient as i " +
            "LEFT OUTER JOIN ingredient_in_tag as iit " +
            "ON (i.id = iit.ingredient_id) " +
            "LEFT OUTER JOIN ingredient_tag as it " +
            "ON (iit.tag_id = it.id) " +
            "WHERE i.id = " + id + " " +
            "GROUP BY i.id, i.name;"
        ).list.headOption
      }
    }
  }

  def find(name: String): Future[Option[Ingredient]] = {
    Future.successful {
      DB withSession { implicit session =>
        Q.queryNA[Ingredient](
          "SELECT i.id, i.name, array_agg(it.name) " +
            "FROM ingredient as i " +
            "LEFT OUTER JOIN ingredient_in_tag as iit " +
            "ON (i.id = iit.ingredient_id) " +
            "LEFT OUTER JOIN ingredient_tag as it " +
            "ON (iit.tag_id = it.id) " +
            "WHERE i.name like %" + name + "% " +
            "GROUP BY i.id, i.name;"
        ).list.headOption
      }
    }
  }

  def save(ingredient: Ingredient): Future[Option[Ingredient]] = {
    val id = DB withTransaction { implicit session =>
      val dbIngredient = DBIngredient(None, ingredient.name)
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
      val dbIngredient = DBIngredient(Some(ingredientId), ingredient.name)
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
        slickIngredientTags.sortBy(_.name.asc).list.map(i => IngredientTag(i.id, i.name))
      }
    }
  }
}
