package repository.exceptions

case class NoSuchFavoriteFoundException(message: String) extends  Exception(message) {
  def this(userId: Long, recipeId: Long) = this("User " + userId + " has not recipe " + recipeId + " in his " +
    "favorites.")
}

object NoSuchFavoriteFoundException {
  def apply(userId: Long, recipeId: Long) = new NoSuchRecipeException("User " + userId + " has not recipe " +
    recipeId +
    " " +
    "in his " +
    "favorites.")
}