package repository.models

case class Stats (
                  numRecipes: Int,
                  numIngredients: Int,
                  numTags: Int,
                  numUsers: Int,
                  numStarRatingsRecipe: Int,
                  numYesNoRatingsRecipe: Int,
                  numYesNoRatingsIngredient: Int
                   )
