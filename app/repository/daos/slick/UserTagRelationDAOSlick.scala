package repository.daos.slick

import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.UserTagRelationDAO
import repository.daos.slick.DBTableDefinitions._
import play.api.Play.current
import repository.services.TagsRec
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import scala.concurrent.Future

class UserTagRelationDAOSlick extends UserTagRelationDAO {

  def __fixValue(oldValue: Double, value: Double): Double = {
    Math.max(Math.min(5, value+oldValue), -5)
  }

  def saveIngredientTagRelation(userId: Long, ingredientTagId: Long, value: Double) = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserIngredientTagRelations.filter(x => x.userId === userId && x.ingredientTagId === ingredientTagId)
          .firstOption match {
          case Some(old) =>
            slickUserIngredientTagRelations.filter(x => x.userId === userId && x.ingredientTagId === ingredientTagId)
              .update(DBUserIngredientTagRelation(userId, ingredientTagId, __fixValue(old.value, value)))
          case None => slickUserIngredientTagRelations.insert(DBUserIngredientTagRelation(userId, ingredientTagId, value))
        }
      }
    }
  }

  def saveRecipeTagRelation(userId: Long, recipeTagId: Long, value: Double) = {
    Future.successful {
      DB withSession { implicit session =>
        slickUserRecipeTagRelations.filter(x => x.userId === userId && x.recipeTagId === recipeTagId)
          .firstOption match {
          case Some(old) =>
            slickUserRecipeTagRelations.filter(x => x.userId === userId && x.recipeTagId === recipeTagId)
              .update(DBUserRecipeTagRelation(userId, recipeTagId, __fixValue(old.value, value)))
          case None => slickUserRecipeTagRelations.insert(DBUserRecipeTagRelation(userId, recipeTagId, value))
        }
      }
    }
  }

  def getIngredientTagValuesForUser(userId: Long): Map[Long, Double] = {
    DB withSession { implicit session =>
      slickUserIngredientTagRelations.filter(_.userId === userId).map(x => x.ingredientTagId -> x.value).toMap
    }
  }

  def getIngredientTagsForRecipe(recipeId: Long): Seq[Long] = {
    DB withSession { implicit session =>
      slickIngredientsInRecipe.filter(_.recipeId === recipeId).map(_.ingredientId).list
    }
  }

  implicit val getRecommendationResult = GetResult(r =>
    RecipeTagRecResult(
      r.nextLong(),
      r.nextDouble(),
      r.nextInt(),
      r.nextString(),
      r.nextInt()
    )
  )

  def retrieveTopRecipesBasedOnTags(N: Int, userId: Long): Seq[RecipeTagRecResult] = {
    DB withSession { implicit session =>
      Q.queryNA[RecipeTagRecResult](
        "SELECT core.id, core.score, recipe.spicy, recipe.difficulty, recipe.time from ( " +
          "SELECT inner_recipes.id, sum(inner_recipes.score) AS score " +
            "FROM ( " +
              "SELECT r.id, sum(uit.value) / count(*) AS score " +
              "FROM recipe AS r " +
              "JOIN ingredient_in_recipe as iir on(r.id = iir.recipe_id) " +
              "JOIN ingredient_in_tag as iit on(iir.ingredient_id = iit.ingredient_id) " +
              "JOIN user_ingredient_tag AS uit on (iit.tag_id = uit.ingredient_tag_id) " +
              "WHERE uit.user_id = " + userId + " " +
              "GROUP BY r.id " +
              "UNION " +
              "SELECT r.id, sum(urt.value) / count(*) AS score " +
              "FROM recipe AS r " +
              "JOIN recipe_in_tag AS rit ON (rit.recipe_id = r.id) " +
              "JOIN user_recipe_tag AS urt ON(urt.recipe_tag_id = rit.tag_id) " +
              "WHERE urt.user_id = " + userId + " " +
              "GROUP BY r.id " +
            ") AS inner_recipes " +
            "GROUP BY inner_recipes.id " +
            "ORDER BY score DESC " +
            "LIMIT " + N + ") AS core " +
        "JOIN recipe ON(core.id = recipe.id);"
      ).list
    }
  }

}
