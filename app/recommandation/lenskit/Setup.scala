package lenskit

import java.sql.Connection
import java.util
import org.grouplens.lenskit.data.sql.JDBCRatingDAO
import org.grouplens.lenskit.scored.ScoredId
import org.grouplens.lenskit.{RecommenderBuildException, ItemScorer}
import org.grouplens.lenskit.baseline.{ItemMeanRatingItemScorer, UserMeanBaseline, UserMeanItemScorer, BaselineScorer}
import org.grouplens.lenskit.core.{LenskitRecommender, LenskitConfiguration}
import org.grouplens.lenskit.transform.normalize.{BaselineSubtractingUserVectorNormalizer, UserVectorNormalizer}

object Setup {

  def run(userId: Long, conn: Connection): util.List[ScoredId] = {

    val jdbcDAO = JDBCRatingDAO.newBuilder()
      .setTableName("user_star_rate_recipe")
      .setUserColumn("user_id")
      .setItemColumn("recipe_id")
      .setRatingColumn("rating")
      .setTimestampColumn("created_long")
      .build(conn)

    val config =  new LenskitConfiguration

    config.addComponent(jdbcDAO)
    config.bind(classOf[ItemScorer]).to(classOf[UserItemBiasItemScorer])
    config.bind(classOf[BaselineScorer], classOf[ItemScorer]).to(classOf[UserMeanItemScorer])
    config.bind(classOf[UserMeanBaseline], classOf[ItemScorer]).to(classOf[ItemMeanRatingItemScorer])
    config.bind(classOf[UserVectorNormalizer]).to(classOf[BaselineSubtractingUserVectorNormalizer])

    try {
      val rec = LenskitRecommender.build(config)
      val irec = rec.getItemRecommender
      irec.recommend(userId, 10)
    } catch {
      case e: RecommenderBuildException => throw new RuntimeException("Recommender build failed", e)
    }
  }

}
