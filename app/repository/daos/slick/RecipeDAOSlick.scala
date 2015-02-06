package repository.daos.slick

import java.io.File

import cloudinary.model.CloudinaryResource
import com.cloudinary.parameters.UploadParameters
import com.google.inject.Inject
import repository.daos.UserDAO
import org.joda.time.LocalDateTime
import play.api.db.slick._
import myUtils.MyPostgresDriver.simple._
import repository.daos.slick.DBTableDefinitions._
import play.api.libs.json.JsValue
import repository.exceptions.{NoSuchUnitException, NoSuchRecipeException}
import repository.daos.{RatingDAO, RecipeDAO}
import repository.models._
import scala.concurrent._
import scala.util.{Try, Failure, Success}
import play.api.libs.concurrent.Execution.Implicits._

class RecipeDAOSlick @Inject() (
  val userDAO: UserDAO,
  val ratingDAO: RatingDAO
                                 ) extends RecipeDAO {
  import play.api.Play.current


  def find(id: Long, userId: Long): Future[Option[Recipe]] = {
    DB withSession { implicit session =>
        slickRecipes.filter(_.id === id)
          .firstOption match {
            case Some(recipe) => transformRecipe(recipe, userId).map(x => Some(x))
            case None => Future { None }
      }
    }
  }

  def transformRecipe(r: DBRecipe, userId: Long): Future[Recipe] = {

      val futures = for {
        iF <- findIngredientsForRecipe(r.id.get)
        tF <- findTagsForRecipe(r.id.get)
        uF <- userDAO.find(r.createdById)
        rF <- ratingDAO.findUserStarRateRecipe(userId, r.id.get)
        avgF <- ratingDAO.getAverageRatingForRecipe(r.id.get)
      } yield (iF, tF, uF, rF, avgF)

      val user = futures map (_._3.get)
      futures.map(x =>
        Recipe(
          r.id,
          r.name,
          r.image,
          r.description,
          r.language,
          r.calories,
          r.procedure,
          r.spicy,
          r.time,
          r.difficulty,
          Some(r.created),
          Some(r.modified),
          r.published,
          r.deleted,
          x._1,
          x._2,
          x._4 match {
            case Some(rating) => Some(rating.stars)
            case None => None
          },
          x._5,
          Some(TinyUser(x._3.get.userID.get, x._3.get.firstName, x._3.get.lastName))))

  }

  def find(q: Option[String], offset: Integer, limit: Integer, published: Option[Boolean], deleted: Option[Boolean],
           language: Option[Long], tag: Option[String]): Future[List[TinyRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
        tag match {
          case Some(tagName) =>
            val join = for {
              (r, t) <- slickRecipes innerJoin slickTagsForRecipe on (_.id === _.recipeId) innerJoin slickTags on (_._2
                .tagId === _.id) if t.name.toLowerCase === tagName.toLowerCase
            } yield (r._1.id, r._1.name, r._1.image, r._1.language, r._1.published, r._1.deleted, r._1.description, r
                ._1.spicy, r._1.time, r._1.difficulty)

            val lFiltered = language match {
              case Some(l) => join.filter(_._4 === l)
              case None => join
            }
            val pFiltered = published match {
              case Some(p) => lFiltered.filter{ r => if (p) r._5.nonEmpty else r._5.isEmpty }
              case None => lFiltered
            }
            val dFiltered = deleted match {
              case Some(d) => pFiltered.filter{ r => if (d) r._6.nonEmpty else r._6.isEmpty }
              case None => pFiltered
            }
            val nFiltered = q match {
              case Some(query) => dFiltered.filter(r => r._2.toLowerCase like "%" + query.toLowerCase + "%")
              case None => dFiltered
            }
            nFiltered
              .drop(offset)
              .take(limit)
              .list
              .map(x => TinyRecipe(x._1.get, x._2, x._3, x._7, x._8, x._9, x._10))
          case None =>
            val lFiltered = language match {
              case Some(l) => slickRecipes.filter(_.language === l)
              case None => slickRecipes
            }
            val pFiltered = published match {
              case Some(p) => lFiltered.filter{ r => if (p) r.published.nonEmpty else r.published.isEmpty }
              case None => lFiltered
            }
            val dFiltered = deleted match {
              case Some(d) => pFiltered.filter{ r => if (d) r.deleted.nonEmpty else r.deleted.isEmpty }
              case None => pFiltered
            }
            val nFiltered = q match {
              case Some(query) => dFiltered.filter(r => r.name.toLowerCase like "%" + query.toLowerCase + "%")
              case None => dFiltered
            }
            nFiltered
              .drop(offset)
              .take(limit)
              .list.map (
                x => TinyRecipe(x.id.get, x.name, x.image, x.description, x.spicy, x.time, x.difficulty)
            )
        }
      }
    }
  }

  def findIngredientsForRecipe(recipeId: Long) : Future[Seq[IngredientForRecipe]] = {
    Future.successful {
      DB withSession { implicit session =>
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
    Future.successful {
      DB withSession { implicit session =>
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
    Future.successful {
      DB withSession { implicit session =>
        val join = for {
          (r, t) <- slickRecipes innerJoin slickTagsForRecipe on (_.id === _.recipeId) innerJoin slickTags on (_._2
            .tagId === _.id) if t.name.toLowerCase === tagName.toLowerCase
        } yield (r._1.id, r._1.name, r._1.image, r._1.description, r._1.spicy, r._1.time, r._1.difficulty)
        join.buildColl[List].map {
          case (id, name, image, description, spicy, time, difficulty) =>
            TinyRecipe(id.get, name, image, description, spicy, time, difficulty)
        }
      }
    }
  }

  def mapToTinyRecipe(recipes: Option[List[Recipe]]): Option[List[TinyRecipe]] = {
    recipes match {
      case Some(r) => Option(r.map{
        x =>
          TinyRecipe(x.id.toList.head, x.name, x.image, x.description, x.spicy, x.time, x.difficulty)
      })
      case None => None
    }
  }

  def transformRecipes(rawRecipes: List[DBRecipe], userId: Long): Future[List[Recipe]] = {

    val res = rawRecipes.map{

      r =>
        val futures = for {
          iF <- findIngredientsForRecipe(r.id.get)
          tF <- findTagsForRecipe(r.id.get)
          uF <- userDAO.find(r.createdById)
          rF <- ratingDAO.findUserStarRateRecipe(userId, r.id.get)
          avgF <- ratingDAO.getAverageRatingForRecipe(r.id.get)
        } yield (iF, tF, uF, rF, avgF)


        futures map(x =>
          Recipe(
            r.id,
            r.name,
            r.image,
            r.description,
            r.language,
            r.calories,
            r.procedure,
            r.spicy,
            r.time,
            r.difficulty,
            Some(r.created),
            Some(r.modified),
            r.published,
            r.deleted,
            x._1,
            x._2,
            x._4 match {
              case Some(rating) => Some(rating.stars)
              case None => None
            },
            x._5,
            Some(TinyUser(x._3.get
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
      find(rid, user.userID.get)
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
                  r.difficulty,
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
                  val ingr = saveIngredient(Ingredient(i.ingredientId, i.name, i.image, Seq()))
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
    find(r.id.get, user.userID.get)
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
        r.time, r.difficulty, new LocalDateTime(), new LocalDateTime(), None, r.deleted, user.userID.get))
      r.ingredients.distinct.foreach {
        i =>
          val ingr = saveIngredient(Ingredient(i.ingredientId, i.name, i.image, Seq()))
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
        Ingredient(Some(iid), i.name, i.image, Seq())
      }
      else {
        Ingredient(ingredients.head.id, i.name, ingredients.head.image, Seq())
      }
    }
  }

  def findIngredients(name: String): Seq[Ingredient] = {
    DB withSession { implicit session =>
      Option(slickIngredients.filter(_.name.toLowerCase === name.toLowerCase).list) match {
        case Some(ingredients) => ingredients.map(i => Ingredient(i.id, i.name, i.image, Seq()))
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


  def deleteRecipe(id: Long, userId: Long): Future[Option[Recipe]] = {
    DB withTransaction { implicit session =>
      slickRecipes.filter(_.id === id).firstOption match {
        case Some(r) =>
          slickRecipes.filter(_.id === r.id).update(
            DBRecipe(r.id, r.name, r.image, r.description, r.language, Some(0), r.procedure, r
              .spicy, r.time, r.difficulty, r.created, new LocalDateTime(), r.published, Some(new LocalDateTime()), r
                .createdById))
        case None => throw new NoSuchRecipeException(id)
      }
    }
    find(id, userId)
  }



}
