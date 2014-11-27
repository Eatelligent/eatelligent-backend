package repository.daos.slick

import repository.daos.IngredientDAO
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import repository.models.Ingredient

import scala.concurrent.Future

class IngredientDAOSlick extends IngredientDAO {
  import play.api.Play.current

  def getAll: Future[Seq[Ingredient]] = {
    DB withSession { implicit session =>
      Future.successful{
        slickIngredients.list.map(i => Ingredient(i.id, i.name, i.image))
      }
    }
  }

  def find(id: Long): Future[Option[Ingredient]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickIngredients.filter(
          i => i.id === id
        ).firstOption match {
          case Some(ingredient) => Some(Ingredient(ingredient.id, ingredient.name, ingredient.image))
          case None => None
        }
      }
    }
  }

  def find(name: String): Future[Option[Ingredient]] = {
    DB withSession { implicit session =>
      Future.successful {
        slickIngredients.filter(
          _.name.toLowerCase like "%" + name.toLowerCase + "%"
        ).firstOption match {
          case Some(ingredient) => Some(Ingredient(ingredient.id, ingredient.name, ingredient.image))
          case None => None
        }
      }
    }
  }

  def save(ingredient: Ingredient): Future[Ingredient] = {
    DB withSession { implicit session =>
      Future.successful {
        val dbIngredient = DBIngredient(ingredient.id, ingredient.name, ingredient.image)
        slickIngredients.filter(_.id === dbIngredient.id).firstOption match {
          case Some(i) => slickIngredients.filter(_.id === dbIngredient.id).update(dbIngredient)
          case None => slickIngredients.insert(dbIngredient)
        }
        ingredient
      }
    }
  }
}
