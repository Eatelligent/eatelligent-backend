package repository.daos.slick

import java.io.File
import java.util.UUID

import cloudinary.model.CloudinaryResource
import com.cloudinary.parameters.UploadParameters
import com.google.inject.Inject
import models.daos.UserDAO
import org.joda.time.LocalDateTime
import play.api.Logger
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import models.daos.slick.DBTableDefinitions._
import play.api.libs.json.JsValue
import repository.Exceptions.{NoSuchUnitException, NoSuchRecipeException}
import repository.daos.RecipeDAO
import repository.models._
import scala.concurrent._
import scala.util.{Try, Failure, Success}
import play.api.libs.concurrent.Execution.Implicits._

class RecipeDAOSlick @Inject() (
  val userDAO: UserDAO
                                 ) extends RecipeDAO {
  import play.api.Play.current


  def find(id: Long): Future[Option[Recipe]] = {
    DB withSession { implicit session =>
        slickRecipes.filter(_.id === id)
          .firstOption match {
            case Some(recipe) => transformRecipe(recipe).map(x => Some(x))
            case None => Future { None }
      }
    }
  }

  def transformRecipe(r: DBRecipe): Future[Recipe] = {

      val futures = for {
        iF <- findIngredientsForRecipe(r.id.get)
        tF <- findTagsForRecipe(r.id.get)
        uF <- userDAO.find(r.createdById)
      } yield (iF, tF, uF)

      val user = futures map (_._3.get)
      futures map(x => Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r.procedure,
        r.spicy, r.time, Some(r.created), Some(r.modified), r.published, r.deleted, x._1, x._2,
        Some(TinyUser(x._3.get.userID.get, x._3.get.firstName, x._3.get.lastName))))

  }

  def find(q: String, offset: Integer, limit: Integer, published: Boolean, deleted: Boolean, language: Long):
  Future[List[TinyRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
          slickRecipes
            .filter(_.language === language)
            .filter(_.name.toLowerCase like "%" + q.toLowerCase + "%")
            .filter{ r => if (published) r.published.nonEmpty else r.published.isEmpty }
            .filter{ r => if (deleted) r.deleted.nonEmpty else r.deleted.isEmpty }
            .drop(offset).take (limit)
            .list.map (
              x => TinyRecipe(x.id.get, x.name, x.image)
          )
      }
    }
  }

  def findIngredientsForRecipe(recipeId: Long) : Future[Seq[IngredientForRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        val join = for {
          ((iir, i), u) <- slickIngredientsInRecipe innerJoin slickIngredients on (_.ingredientId === _.id) innerJoin
            slickUnits on (_._1.unitId === _.id) if iir.recipeId === recipeId
        } yield (iir.ingredientId, i.name, i.image, u.name, iir.amount)

        val l: Seq[(Long, String, Option[JsValue], String, Double)] = join.buildColl[List]
        l.map{
          case (iid, name, image, unit, amount) => IngredientForRecipe(Some(iid), name, image, unit, amount)
        }
      }
    }
  }

  def findTagsForRecipe(recipeId: Long): Future[Seq[String]] = {
    DB withSession { implicit session =>
      Future.successful {
        val join = for {
          (tfr, t) <- slickTagsForRecipe innerJoin slickTags on (_.tagId === _.id) if tfr.recipeId === recipeId
        } yield (t.id, t.name)

        val l = join.buildColl[List]
        val res = l.map {
          case (id, name) => name
        }
        res
      }
    }
  }


  def findRecipesInTag(tagName: String): Future[Seq[TinyRecipe]] = {
    // select * from recipe as r join recipe_in_tag on r.id = recipe_id join tags as t on t.id = tag_id WHERE t.name = 'norsk';
    DB withSession { implicit session =>
      Future.successful {
        val join = for {
          (r, t) <- slickRecipes innerJoin slickTagsForRecipe on (_.id === _.recipeId) innerJoin slickTags on (_._2
            .tagId === _.id) if t.name.toLowerCase === tagName.toLowerCase
        } yield (r._1.id, r._1.name, r._1.image)
        join.buildColl[List].map(r => TinyRecipe(r._1.get, r._2, r._3))
      }
    }
  }

  def mapToTinyRecipe(recipes: Option[List[Recipe]]): Option[List[TinyRecipe]] = {
    recipes match {
      case Some(r) => Option(r.map{
        x =>
          TinyRecipe(x.id.toList.head, x.name, x.image)
      })
      case None => None
    }
  }

  def transformRecipes(rawRecipes: List[DBRecipe]): Future[List[Recipe]] = {

    val res = rawRecipes.map{

      r =>
        val futures = for {
          iF <- findIngredientsForRecipe(r.id.last)
          tF <- findTagsForRecipe(r.id.last)
          uF <- userDAO.find(r.createdById)
        } yield (iF, tF, uF)


        futures map(x => Recipe(r.id, r.name, r.image, r.description, r.language, r.calories, r
          .procedure, r.spicy, r.time,
          Some(r.created), Some(r.modified), r.published, r.deleted, x._1, x._2, Some(TinyUser(x._3.get
            .userID.get, x._3
            .get
            .firstName, x._3.get.lastName))))
    }

    val listOfTrys = res.map(futureToFutureTry(_))
    val futureListOftrys = Future.sequence(listOfTrys)
    futureListOftrys.map(_.collect{ case Success(x) => x})
  }

  def futureToFutureTry[T](f: Future[T]): Future[Try[T]] =
    f.map(Success(_)).recover({case x => Failure(x)})

  def updateImage(image: RecipeImage) {
    DB withSession { implicit session =>
      val q = for { r <- slickRecipes if r.id === image.recipeId } yield r.image

      val a = q.run
      //    if (a.head.nonEmpty)
      //      deleteImage
      q.update(Some(image.url))

    }
  }

  //  def deleteImage = ???

  def save(r: Recipe, user: User): Future[Option[Recipe]] = {
    DB withSession { implicit session =>
      val rid: Long = insert(r, user)
      find(rid)
    }
  }

  def update(r: Recipe, user: User): Future[Option[Recipe]] = {
    DB withTransaction { implicit session =>

      r.id match {
        case Some(id) =>
          slickRecipes.filter(_.id === id).firstOption match {
            case Some(recipeFound) =>
              slickIngredientsInRecipe.filter(_.recipeId === recipeFound.id).delete
              slickTagsForRecipe.filter(_.recipeId === recipeFound.id).delete
              slickRecipes.filter(_.id === recipeFound.id).update(
                DBRecipe(
                  r.id,
                  r.name,
                  r.image,
                  r.description,
                  r.language,
                  Some(0),
                  r.procedure,
                  r.spicy,
                  r.time,
                  recipeFound.created,
                  new LocalDateTime(),
                  r.published match {
                    case Some(p) =>
                      if (recipeFound.published.isDefined) recipeFound.published
                      else Some(new LocalDateTime())
                    case None => None
                  },
                  r.deleted match {
                    case Some(d) =>
                      if (recipeFound.deleted.isDefined) recipeFound.deleted
                      else Some(new LocalDateTime)
                    case None => None
                  },
                  user.userID.get))
              r.ingredients.distinct.foreach {
                i =>
                  val ingr = saveIngredient(Ingredient(i.ingredientId, i.name, i.image))
                  slickIngredientsInRecipe.insert(DBIngredientInRecipe(recipeFound.id.get, ingr.id.get, getUnitId(i
                    .unit), i
                    .amount))
              }
              r.tags.distinct.foreach {
                t =>
                  val tag = saveTag(t)
                  slickTagsForRecipe.insert(DBTagForRecipe(recipeFound.id.get, tag.id.get))
              }
            case None => throw NoSuchRecipeException(r.id)
          }
        case None => throw NoSuchRecipeException(r.id)
      }
    }
    find(r.id.get)
  }

  def getUnitId(name: String): Long = {
    DB withSession { implicit session =>
      slickUnits.filter(_.name === name).firstOption match {
        case Some(u) => u.id.get
        case None => throw new NoSuchUnitException(name)
      }
    }
  }


  def insert(r: Recipe, user: User): Long = {
    val id = DB withTransaction { implicit session =>
      val rid = insertRecipe(DBRecipe(r.id, r.name, r.image, r.description, r.language, Some(0), r
        .procedure, r.spicy,
        r.time, new LocalDateTime(), new LocalDateTime(), None, r.deleted, user.userID.get))
      r.ingredients.distinct.foreach {
        i =>
          val ingr = saveIngredient(Ingredient(i.ingredientId, i.name, i.image))
          slickIngredientsInRecipe.insert(DBIngredientInRecipe(rid, ingr.id.get, getUnitId(i.unit), i.amount))
      }
      r.tags.distinct.foreach {
        t =>
          val tag = saveTag(t)
          slickTagsForRecipe.insert(DBTagForRecipe(rid, tag.id.get))
      }
      rid
    }
    id
  }



  def saveIngredient(i: Ingredient): Ingredient = {
    DB withSession { implicit session =>
      val ingredients = findIngredients(i.name)
      if (ingredients.isEmpty) {
        val iid = insertIngredient(DBIngredient(None, i.name, i.image))
        Ingredient(Some(iid), i.name, i.image)
      }
      else {
        Ingredient(ingredients.head.id, i.name, ingredients.head.image)
      }
    }
  }

  def findIngredients(name: String): Seq[Ingredient] = {
    DB withSession { implicit session =>
      Option(slickIngredients.filter(_.name.toLowerCase === name.toLowerCase).list) match {
        case Some(ingredients) => ingredients.map(i => Ingredient(i.id, i.name, i.image))
        case None => List()
      }
    }
  }

  def saveTag(name: String): DBTag = {
    DB withSession { implicit session =>
      val tags = findTags(name)
      if (tags.isEmpty) {
        val tid = insertTag(DBTag(None, name))
        DBTag(Some(tid), name)
      }
      else {
        DBTag(tags.head.id, name)
      }
    }
  }

  def findTags(name: String): Seq[RecipeTag] = {
    DB withSession { implicit session =>
        Option(slickTags.filter(_.name.toLowerCase like "%" + name.toLowerCase + "%").list) match {
          case Some(tags) => tags.map(tag => RecipeTag(tag.id, tag.name))
          case None => List()
        }
    }
  }

  def saveImage(id: Long, image: File): Future[Option[RecipeImage]] = {
    val recipeExists = DB withSession { implicit session =>
      slickRecipes.filter(_.id === id).length.run > 0
    }
    if (!recipeExists)
      throw new NoSuchRecipeException("No recipe with id: " + id + " in the databse.")
    CloudinaryResource.upload(image, UploadParameters().faces(true).colors(true).imageMetadata(true).exif
      (true)).map {
      cr =>
        val image = RecipeImage(id, cr.url())
        updateImage(image)
        Some(image)
    }
  }


  def deleteRecipe(id: Long): Future[Option[Recipe]] = {
    DB withTransaction { implicit session =>
      slickRecipes.filter(_.id === id).firstOption match {
        case Some(r) =>
          slickRecipes.filter(_.id === r.id).update(
            DBRecipe(r.id, r.name, r.image, r.description, r.language, Some(0), r.procedure, r
              .spicy,
              r.time, r.created, new LocalDateTime(), r.published, Some(new LocalDateTime()), r.createdById))
        case None => throw new NoSuchRecipeException(id)
      }
    }
    find(id)
  }



}
