package lenskit

import org.grouplens.lenskit.data.sql.JDBCRatingDAO
import org.grouplens.lenskit.{RecommenderBuildException, ItemScorer}
import org.grouplens.lenskit.baseline.{ItemMeanRatingItemScorer, UserMeanBaseline, UserMeanItemScorer, BaselineScorer}
import org.grouplens.lenskit.core.{LenskitRecommender, LenskitConfiguration}
import org.grouplens.lenskit.transform.normalize.{BaselineSubtractingUserVectorNormalizer, UserVectorNormalizer}
import play.api.db.DB
import repository.services.CFRec
import scala.collection.JavaConversions._
import play.api.Play.current


object Setup {

  def run(userId: Long, limit: Int): Seq[CFRec] = {

    val conn: java.sql.Connection = DB.getConnection()
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
      irec.recommend(userId, limit)
      .map(x => CFRec(userId, x.getId, "CF", x.getScore))
    } catch {
      case e: RecommenderBuildException => throw new RuntimeException("Recommender build failed", e)
    } finally {
      conn.close()
    }

  }

}
