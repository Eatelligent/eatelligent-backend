package repository.exceptions

case class DuplicateException(message: String) extends  Exception(message) {
  def this() = this("This entry already exists.")
}

object DuplicateException {
  def apply() = new NoSuchRecipeException("This entry already exists.")
}