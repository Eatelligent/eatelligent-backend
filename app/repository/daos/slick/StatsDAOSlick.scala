package repository.daos.slick

import repository.daos.StatsDAO
import repository.models.Stats
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import play.api.Play.current

import scala.concurrent.Future

class StatsDAOSlick extends StatsDAO {

  def getTotalStats: Future[Stats] = {
    DB withSession { implicit session =>
      Future.successful {
        val numRecipes = slickRecipes.length.run
        val numIngredients = slickIngredients.length.run
        val numTags = slickTags.length.run
        val numUsers = slickUsers.length.run
        val numStarRatingsRecipe = slickUserStarRateRecipes.length.run
        val numYesNoRatingsRecipe = slickUserYesNoRateRecipes.length.run
        val numYesNoRatingsIngredient = slickUserYesNoRateIngredient.length.run
        Stats(numRecipes, numIngredients, numTags, numUsers, numStarRatingsRecipe, numYesNoRatingsRecipe,
          numYesNoRatingsIngredient)
      }
    }
  }

}
