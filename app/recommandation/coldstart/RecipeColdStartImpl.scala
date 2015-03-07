package recommandation.coldstart

class RecipeColdStartImpl {

  // TODO: oppskriftene trenger å ha avg. rating for at detta skal funke.
  def findRange(thisReipe: RecipeColdStartModel, allRecipes: Seq[RecipeColdStartModel]): RecipePrediction = {
    val recipe = allRecipes.fold(allRecipes(0)) { (recipe, acc) => {
        if(thisReipe != recipe && sim(thisReipe, acc) < sim(thisReipe, recipe)) {
          recipe
        } else {
          acc
        }
      }
    }

    val similarity = sim(recipe, thisReipe)
    val avg = recipe.averageRating
    val upper = if (similarity == 0) 5 else Math.min(5, avg * (1 / similarity))

    RecipePrediction(
      Math.max(1, avg * similarity),
      upper,
      thisReipe.id,
      recipe.id
    )
  }

  def sim(thisRecipe: RecipeColdStartModel, otherRecipe: RecipeColdStartModel): Double = {
    val diff = (thisRecipe.recipeTags.diff(otherRecipe.recipeTags) ++
      otherRecipe.ingredientTags.diff(thisRecipe.recipeTags)).length +
      (thisRecipe.ingredientTags.diff(otherRecipe.ingredientTags) ++ otherRecipe.ingredientTags.diff(thisRecipe
        .ingredientTags)).length

    val N = thisRecipe.recipeTags.union(otherRecipe.recipeTags).length +
    thisRecipe.ingredientTags.union(otherRecipe.ingredientTags).length

    val denominator = if (N == 0) 0 else diff / N.toDouble
    if (diff == 0) 1.0 else 1.0 / (denominator + 1)
  }
}


case class RecipeColdStartModel(
                               id: Long,
                               recipeTags: Seq[Long],
                               ingredientTags: Seq[Long],
                               averageRating: Double
                               )

case class  RecipePrediction(
                       low: Double,
                       upper: Double,
                       recipeId: Long,
                       similarRecipe: Long
                       )
