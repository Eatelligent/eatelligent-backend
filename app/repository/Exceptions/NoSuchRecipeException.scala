package repository.Exceptions

case class NoSuchRecipeException(message: String) extends  Exception(message) {
  def this(id: Long) = this("No recipe with id: " + id + " was found.")
}

object NoSuchRecipeException {
  def apply(id: Option[Long]) = new NoSuchRecipeException("No recipe with id: " + id + " was found.")
}