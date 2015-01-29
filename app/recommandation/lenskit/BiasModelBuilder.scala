package lenskit

import org.grouplens.lenskit.data.dao.EventDAO
import org.grouplens.lenskit.data.event.Rating
import org.grouplens.lenskit.util.IdMeanAccumulator
import javax.inject.Inject
import javax.inject.Provider
import scala.collection.JavaConversions._

class BiasModelBuilder @Inject()(dao: EventDAO) extends Provider[BiasModel] {

  def get = {
    val accum = new IdMeanAccumulator
    val ratings = dao.streamEvents(classOf[Rating])

    try {
      for (r: Rating <- ratings.fast()) {
        val pref = r.getPreference
        if (pref != null) {
          accum.put(pref.getItemId, pref.getValue)
        }
      }
    }
    finally {
      ratings.close()
    }
    new BiasModel(accum.globalMean(), accum.idMeanOffsets(5))
  }


}
