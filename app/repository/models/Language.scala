package repository.models

case class Language(
                     id: Option[Long] = None,
                     locale: String,
                     name: String
                     )
