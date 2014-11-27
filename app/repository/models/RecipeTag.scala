package repository.models

case class RecipeTag(
                      id: Option[Long],
                      name: String
                      ) {
  override def equals(o: Any) = o match {
    case that: RecipeTag => that.name.equalsIgnoreCase(this.name)
    case _ => false
  }
  override def hashCode = name.toUpperCase.hashCode
}
