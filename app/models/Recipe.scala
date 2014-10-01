package models

import play.api.libs.json.Json

case class Recipe(name: String)

object Recipe {
  implicit val personFormat = Json.format[Recipe]
}
