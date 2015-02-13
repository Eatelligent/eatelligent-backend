package repository.exceptions

case class NotFoundException(message: String) extends  Exception(message) {
  def this(id: Long) = this("Not found.")
}

object NotFoundException {
  def apply(id: Option[Long]) = new NotFoundException("Not found.")
}
