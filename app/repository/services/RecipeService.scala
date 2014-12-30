package repository.services

import java.io.File
import repository.models.{RecipeImage, TinyRecipe, Recipe, User}

import scala.concurrent.Future

trait RecipeService {

  def save(recipe: Recipe, user: User): Future[Option[Recipe]]

  def update(r: Recipe, user: User): Future[Option[Recipe]]

  def find(id: Long): Future[Option[Recipe]]

  def find(name: String): Future[List[TinyRecipe]]

  def getAll(offset: Integer, limit: Integer, published: Boolean, deleted: Boolean): Future[Seq[TinyRecipe]]

  def saveImage(id: Long, image: File): Future[Option[RecipeImage]]

  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]]

  def deleteRecipe(id: Long): Future[Option[Recipe]]

}
