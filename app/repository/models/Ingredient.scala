package repository.models

case class Ingredient(
                             id: Option[Long],
                             name: String,
                             tags: Seq[String]
                             ) {
  override def equals(o: Any) = o match {
    case that: Ingredient => that.name.equalsIgnoreCase(this.name)
    case _ => false
  }
  override def hashCode = name.toUpperCase.hashCode
}
