package repository.daos

import java.io.File
import repository.models.{RecipeImage, TinyRecipe, Recipe, User}

import scala.concurrent.Future

trait RecipeDAO {

  def find(id: Long, userId: Long): Future[Option[Recipe]]

  def find(q: Option[String], offset: Integer, limit: Integer, published: Option[Boolean], deleted: Option[Boolean],
           language: Option[Long], tag: Option[String]): Future[List[TinyRecipe]]

  def save(recipe: Recipe, user: User): Future[Option[Recipe]]

  def update(r: Recipe, user: User): Future[Option[Recipe]]

  def saveImage(id: Long, image: File): Future[Option[RecipeImage]]

  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]]

  def deleteRecipe(id: Long, userId: Long): Future[Option[Recipe]]

}
