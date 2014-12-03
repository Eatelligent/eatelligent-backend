package repository.daos.slick

import org.joda.time.LocalDateTime
import repository.daos.StatsDAO
import repository.models.{DateStats, Stats}
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import play.api.Play.current
import scala.slick.jdbc.{GetResult, StaticQuery => Q}

import scala.concurrent.Future

class StatsDAOSlick extends StatsDAO {

  implicit val getUserResult = GetResult(r => DateStats(new LocalDateTime(r.nextDate()), r.nextInt()))

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

  def getUserStats(from: LocalDateTime, to: LocalDateTime): Future[Seq[DateStats]] = {
    DB withSession { implicit session =>
      Future.successful {
        Q.queryNA[DateStats]("SELECT to_char(created, 'YYYY-MM-DD') AS date, count(*) AS number FROM users " +
          "WHERE to_char(created, 'YYYY-MM-DD') > '" + from.toLocalDate.toString + "' " +
          "AND to_char(created, 'YYYY-MM-DD') < '" + to.toLocalDate.toString + "' " +
          "GROUP " +
          "BY " +
          "to_char" +
          "(created, 'YYYY-MM-DD');")
        .list
      }
    }
  }

  def getRatingStats(from: LocalDateTime, to: LocalDateTime): Future[Seq[DateStats]] = {
    DB withSession { implicit session =>
      Future.successful {
        Q.queryNA[DateStats]("SELECT to_char(created, 'YYYY-MM-DD') AS date, count(*) AS number " +
          "FROM user_star_rate_recipe " +
          "WHERE to_char(created, 'YYYY-MM-DD') > '" + from.toLocalDate.toString + "' " +
          "AND to_char(created, 'YYYY-MM-DD') < '" + to.toLocalDate.toString + "' " +
          "GROUP " +
          "BY " +
          "to_char" +
          "(created, 'YYYY-MM-DD');")
          .list
      }
    }
  }
}
