package repository

import myUtils.WithMyDriver


case class Language(
                     id: Option[Int] = None,
                     locale: String,
                     name: String
                     )

trait LanguageComponent extends WithMyDriver {
  import driver.simple._

  class Languages(tag: Tag) extends Table[Language](tag, "language") {
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def locale = column[String]("locale")
    def name = column[String]("name")

    def * = (id, locale, name) <> (Language.tupled, Language.unapply)
  }

  val languages = TableQuery[Languages]

  private val languagesAutoInc = languages returning languages.map(_.id) into {
    case (l, id) => l.copy(id = id)
  }

  def insert(language: Language)(implicit session: Session): Language =
    languagesAutoInc.insert(language)


}
