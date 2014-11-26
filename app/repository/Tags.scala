package repository

import myUtils.WithMyDriver

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

//case class TagSchema(
//                        id: Option[Long],
//                        name: String
//                      )

case class TagForRecipe(
                      recipeId: Long,
                      tagId: Long
)



//
//trait TagComponent extends WithMyDriver {
//  import driver.simple._
//
//  class Tags(tag: Tag) extends Table[TagSchema](tag, "tags") {
//    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
//    def name = column[String]("name")
//
//    def * = (id, name) <> (TagSchema.tupled, TagSchema.unapply)
//  }
//
//  class TagsForRecipe(tag: Tag) extends Table[TagForRecipe](tag, "recipe_in_tag") {
//    def recipeId = column[Long]("recipe_id")
//    def tagId = column[Long]("tag_id")
//
//    def * = (recipeId, tagId) <> (TagForRecipe.tupled, TagForRecipe.unapply)
//  }
//
//  val tags = TableQuery[Tags]
//  val tagsInRecipes = TableQuery[TagsForRecipe]
//
//  def insertTag(tag: TagSchema)(implicit session: Session): Long = {
//    (tags returning tags.map(_.id) += tag).toList.head
//  }
//
//  def findTagById(id: Long)(implicit session: Session): Option[TagSchema] = {
//    tags.filter(_.id === id).list.headOption
//  }
//
//  def findTagsForRecipe(recipeId: Long)(implicit session: Session): Seq[TagSchema] = {
//    val join = for {
//      (tfr, t) <- tagsInRecipes innerJoin tags on (_.tagId === _.id) if tfr.recipeId === recipeId
//    } yield (t.id, t.name)
//
//    val l = join.buildColl[List]
//    val res = l.map {
//      case (id, name) => TagSchema(id, name)
//    }
//    println("hei: " + res)
//    res
//  }

//  def findRecipesInTag(tagId: Long)(implicit session: Session): Seq[Recipe] = {
//    val join =  for {
//      (tfr, r) <- tagsInRecipes innerJoin recipes on (_.recipeId === _.id) if tfr.tagId === tagId
//    } yield Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure, r.created, r.modified,
//        r.ingredients, r.tags)
//    join.buildColl
//  }

//}