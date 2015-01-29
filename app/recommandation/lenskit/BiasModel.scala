package lenskit

import org.grouplens.grapht.annotation.DefaultProvider
import org.grouplens.lenskit.core.Shareable
import org.grouplens.lenskit.vectors.SparseVector

@DefaultProvider(classOf[BiasModelBuilder])
@Shareable
class BiasModel(val mean: Double, val biases: SparseVector) extends Serializable {


  val itemBiases = biases.immutable()

  def getGlobalMean = mean

  def getItemBiases = itemBiases

}
