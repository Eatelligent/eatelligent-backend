package repository.models

import repository.services.RecommendationMetadata

case class RecommendedRecipe(
                         recommendationMetadata: RecommendationMetadata,
                         recipe: TinyRecipe
                           )
