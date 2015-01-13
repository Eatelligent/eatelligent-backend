package repository.Exceptions

case class NoSuchUnitException(message: String) extends Exception(message) {
  def this(name: Long) = this("No unit with name: " + name + " was found.")
}

object NoSuchUnitException {
  def apply(name: Option[String]) = new NoSuchUnitException("No recipe with name: " + name + " was found.")
}
