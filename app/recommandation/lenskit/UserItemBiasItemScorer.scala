package lenskit

import javax.annotation.Nonnull
import javax.inject.Inject

import org.grouplens.lenskit.basic.AbstractItemScorer
import org.grouplens.lenskit.data.dao.UserEventDAO

import org.grouplens.lenskit.{RecommenderBuildException, ItemScorer}
import org.grouplens.lenskit.data.event.{Ratings, Rating}
import org.grouplens.lenskit.vectors.MutableSparseVector

class UserItemBiasItemScorer @Inject()(val model: BiasModel, val userEventDAO: UserEventDAO) extends AbstractItemScorer {

  def score(user: Long, @Nonnull scores: MutableSparseVector): Unit = {
    scores.fill(model.getGlobalMean)
    scores.add(model.itemBiases)
    val ratings = userEventDAO.getEventsForUser(user, classOf[Rating])
    if (ratings != null) {

      val userVector = Ratings.userRatingVector(ratings)
      userVector.add(-model.getGlobalMean)
      userVector.addScaled(model.getItemBiases, -1)
      scores.add(userVector.mean())
    }
  }

}
