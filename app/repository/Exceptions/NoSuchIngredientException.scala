package repository.exceptions

case class NoSuchIngredientException(message: String) extends  Exception(message) {
  def this(id: Long) = this("No ingredient with id: " + id + " was found.")
}

object NoSuchIngredientException {
  def apply(id: Option[Long]) = new NoSuchIngredientException("No ingredient with id: " + id + " was found.")
}
