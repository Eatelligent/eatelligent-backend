package repository.models

import repository.services.RecommendationMetadata

case class Recommendation(
                         recommendationMetadata: RecommendationMetadata,
                         recipe: TinyRecipe
                           )
