package repository.models

import play.api.libs.json.JsValue

case class Ingredient(
                             id: Option[Long],
                             name: String,
                             image: Option[JsValue],
                             tags: Seq[String]
                             ) {
  override def equals(o: Any) = o match {
    case that: Ingredient => that.name.equalsIgnoreCase(this.name)
    case _ => false
  }
  override def hashCode = name.toUpperCase.hashCode
}
