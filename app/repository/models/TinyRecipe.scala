package repository.models

case class TinyRecipe(
                       id: Long,
                       name: String,
                       image: Option[String],
                       description: Option[String],
                       spicy: Option[Int],
                       time: Option[Int],
                       difficulty: Option[String]
                       )
