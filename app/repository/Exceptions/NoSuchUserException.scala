package repository.exceptions

case class NoSuchUserException(message: String) extends  Exception(message) {
  def this(id: Long) = this("No user with id: " + id + " was found.")
}

object NoSuchUserException {
  def apply(id: Option[Long]) = new NoSuchUserException("No user with id: " + id + " was found.")
}