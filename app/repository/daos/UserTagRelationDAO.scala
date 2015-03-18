package repository.daos


trait UserTagRelationDAO {

  def saveIngredientTagRelation(userId: Long, ingredientTagId: Long, value: Double)

  def saveRecipeTagRelation(userId: Long, ingredientTagId: Long, value: Double)

  def retrieveTopRecipesBasedOnTags(N: Int, userId: Long): Seq[RecipeTagRecResult]

  def updateTagValuesForUser(recipeId: Long, userId: Long, delta: Double): Unit

  case class RecipeTagRecResult (
                                  id: Long,
                                  score: Double,
                                  spicy: Int,
                                  difficulty: String,
                                  time: Int
                                  )

}
