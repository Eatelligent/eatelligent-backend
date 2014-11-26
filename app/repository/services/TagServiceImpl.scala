package repository.services

import com.google.inject.Inject
import repository.RecipeTag
import repository.daos.TagDAO

class TagServiceImpl @Inject() (tagDAO: TagDAO) extends TagService {

  def save(tag: RecipeTag) = tagDAO.save(tag)

  def getAll() = tagDAO.getAll()

}
