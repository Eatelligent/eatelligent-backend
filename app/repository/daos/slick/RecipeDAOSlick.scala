package repository.daos.slick

import java.io.File
import java.util.UUID

import cloudinary.model.CloudinaryResource
import com.cloudinary.parameters.UploadParameters
import com.google.inject.Inject
import models.daos.UserDAO
import org.joda.time.DateTime
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import play.api.libs.json.JsValue
import repository.Exceptions.NoSuchRecipeException
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
        slickRecipes.filter(
          r => r.id === id
        ).firstOption match {
          case Some(recipe) => transformRecipe(recipe).map(x => Some(x))
          case None => Future { None }
      }
    }
  }

  def transformRecipe(r: DBRecipe): Future[Recipe] = {

      val futures = for {
        iF <- findIngredientsForRecipe(r.id.get)
        tF <- findTagsForRecipe(r.id.get)
        uF <- userDAO.find(UUID.fromString(r.createdById))
      } yield (iF, tF, uF)

      val user = futures map (_._3.get)
      futures map(x => Recipe(r.id, r.name, r.image, r.description, r.language, Some(r.calories), r.procedure,
        r.spicy, Some(r.created), Some(r.modified), x._1, x._2, Some(TinyUser(x._3.get.userID.toString, x._3.get
          .firstName, x._3.get.lastName))))

  }


  def find(q: String): Future[List[TinyRecipe]] = {
    DB withSession { implicit session =>
      Future.successful( {
        val res = Option(slickRecipes.filter(_.name.toLowerCase like "%" + q.toLowerCase + "%").list)
        res match {
          case Some(r) => r.map(x => TinyRecipe(x.id.get, x.name, x.image))
          case None => List()
        }
      })
    }
  }

  def findIngredientsForRecipe(recipeId: Long) : Future[Seq[IngredientForRecipe]] = {
    DB withSession { implicit session =>
      Future.successful {
        val join = for {
          (iir, i) <- slickIngredientsInRecipe innerJoin slickIngredients on (_.ingredientId === _.id) if iir.recipeId === recipeId
        } yield (iir.ingredientId, i.name, i.image, iir.amount)

        val l: Seq[(Long, String, Option[JsValue], Double)] = join.buildColl[List]
        l.map{
          case (iid, name, image, amount) => IngredientForRecipe(Some(iid), name, image, amount)
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
            .tagId === _.id) if t.name === tagName
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
          uF <- userDAO.find(UUID.fromString(r.createdById))
        } yield (iF, tF, uF)


        futures map(x => Recipe(r.id, r.name, r.image, r.description, r.language, Some(r.calories), r
          .procedure, r.spicy,
          Some(r.created), Some(r.modified), x._1, x._2, Some(TinyUser(x._3.get.userID.toString, x._3.get
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
    val id = DB withTransaction { implicit session =>
      val rid = insertRecipe(DBRecipe(r.id, r.name, r.image, r.description, r.language, 0, r.procedure, r.spicy,
        new DateTime(), new DateTime(), user.userID.toString))
      r.ingredients.distinct.foreach {
        i =>
          val ingr = saveIngredient(Ingredient(i.ingredientId, i.name, i.image))
          slickIngredientsInRecipe.insert(DBIngredientInRecipe(rid, ingr.id.get, i.amount))
      }
      r.tags.distinct.foreach {
        t =>
          val tag = saveTag(t)
          slickTagsForRecipe.insert(DBTagForRecipe(rid, tag.id.get))
      }
      rid
    }
    find(id)
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

  def getAll: Future[Seq[TinyRecipe]] = find("")


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



}
