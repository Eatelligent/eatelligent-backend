package repository.services

import java.io.File

import com.google.inject.Inject
import repository.models.{RecipeImage, Recipe, TinyRecipe, User}
import repository.daos.RecipeDAO
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class RecipeServiceImpl @Inject() (recipeDAO: RecipeDAO) extends RecipeService {

  def save(recipe: Recipe, user: User): Future[Option[Recipe]] = {

    recipe.id match {
      case Some(id) =>
        recipeDAO.find(id, user.userID.get).flatMap {
          case Some(r) =>
            recipeDAO.save(recipe.copy(
              name = recipe.name
            ), user)
          case None =>
            recipeDAO.save(recipe, user)
        }
      case None => recipeDAO.save(recipe, user)
    }
  }

  def update(r: Recipe, user: User): Future[Option[Recipe]] = recipeDAO.update(r, user)

  def find(id: Long, userId: Long): Future[Option[Recipe]] = recipeDAO.find(id, userId)

  def find(query: String, offset: Integer, limit: Integer, published: Boolean, deleted: Boolean, language: Long, tag: Option[String]):
  Future[Seq[TinyRecipe]] =
    recipeDAO.find(query, offset, limit, published, deleted, language, tag)

  def saveImage(id: Long, image: File): Future[Option[RecipeImage]] = recipeDAO.saveImage(id, image)

  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]] = recipeDAO.findRecipesInTag(tagName)

  def deleteRecipe(id: Long, userId: Long): Future[Option[Recipe]] = recipeDAO.deleteRecipe(id, userId)
}
