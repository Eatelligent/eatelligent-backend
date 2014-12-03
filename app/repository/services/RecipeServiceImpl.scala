package repository.services

import java.io.File

import com.google.inject.Inject
import repository.models.{RecipeImage, Recipe, TinyRecipe, User}
import repository.daos.RecipeDAO

import scala.concurrent.Future

class RecipeServiceImpl @Inject() (recipeDAO: RecipeDAO) extends RecipeService {

  def save(recipe: Recipe, user: User): Future[Option[Recipe]] = {
    recipeDAO.save(recipe, user)
  }

  def find(id: Long): Future[Option[Recipe]] = recipeDAO.find(id)

  def find(name: String): Future[List[TinyRecipe]] = recipeDAO.find(name)

  def getAll(offset: Integer, limit: Integer, published: Boolean, deleted: Boolean): Future[Seq[TinyRecipe]] =
    recipeDAO.getAll(offset, limit, published, deleted)

  def saveImage(id: Long, image: File): Future[Option[RecipeImage]] = recipeDAO.saveImage(id, image)

  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]] = recipeDAO.findRecipesInTag(tagName)
}