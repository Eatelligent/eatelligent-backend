package repository

import java.io.File
import cloudinary.model.CloudinaryResource
import com.cloudinary.Transformation
import com.cloudinary.Implicits._
import myUtils.WithMyDriver
import scala.concurrent._
import org.joda.time.DateTime
import com.cloudinary.parameters.UploadParameters
import lib.MappedColumnTypes._
import ExecutionContext.Implicits.global

case class Image(recipeId:Long, image:CloudinaryResource)

case class RecipeImage(recipeId:Long, url: String) {
}

case class IngredientImage(ingredientId:Long, image:CloudinaryResource) {
  def url = image.url()
  def thumbnailUrl = image.url(Transformation().w_(150).h_(150).c_("fit").quality(80))
}

//trait ImageComponent extends WithMyDriver {
//  import driver.simple._
////  import play.api.db.slick.Config.driver.simple._
//  import com.cloudinary.Implicits._
//
//  class Photos(tag: Tag) extends Table[Photo](tag, "photos"){
//    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
//    def title = column[String]("title")
//    def image = column[CloudinaryResource]("image")
//    def bytes = column[Int]("bytes")
//    def createdAt = column[DateTime]("created_at")
//    def * = (id, image, createdAt) <> (Photo.tupled, Photo.unapply)
//  }
//
//
//}