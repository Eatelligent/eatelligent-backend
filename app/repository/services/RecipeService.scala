package repository.services

import java.io.File

import models.User
import repository.{RecipeImage, TinyRecipe, Recipe}

import scala.concurrent.Future

trait RecipeService {

  def save(recipe: Recipe, user: User): Future[Option[Recipe]]

  def find(id: Long): Future[Option[Recipe]]

  def find(name: String): Future[List[TinyRecipe]]

  def getAll: Future[Seq[TinyRecipe]]

  def saveImage(id: Long, image: File): Future[RecipeImage]

  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]]

}
