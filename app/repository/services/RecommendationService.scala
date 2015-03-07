package repository.services

trait RecommendationService {

  def getRecommendations(userId: Long): Seq[RecommendationMetadata]

}
