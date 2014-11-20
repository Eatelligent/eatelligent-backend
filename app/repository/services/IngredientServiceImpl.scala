package repository.services

import com.google.inject.Inject
import repository.Ingredient
import repository.daos.IngredientDAO

import scala.concurrent.Future

class IngredientServiceImpl @Inject()(
  ingredientDAO: IngredientDAO
                                       ) extends IngredientService {

  override def save(ingredient: Ingredient): Future[Ingredient] = ingredientDAO.save(ingredient)

  override def find(id: Long): Future[Option[Ingredient]] = ingredientDAO.find(id)

  override def find(name: String): Future[Option[Ingredient]] = ingredientDAO.find(name)

  override def getAll: Future[Seq[Ingredient]] = ingredientDAO.getAll
}
