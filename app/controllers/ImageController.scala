package controllers

import play.api.libs.json.{JsError, Json}
import repository.{Photo}

import scala.concurrent._
import play.api.mvc.Action
import repository.current.dao._
import play.api.data._
import play.api.data.Forms._
import cloudinary.model.CloudinaryResource
import cloudinary.model.CloudinaryResource.preloadedFormatter
import com.cloudinary.parameters.UploadParameters
import play.api.db.slick._
import play.api.Play.current
import ExecutionContext.Implicits.global


object ImageController extends MyController {


  val directUploadForm = Form(
    mapping(
      "recipeId" -> longNumber,
      "image" -> of[CloudinaryResource]
    )(Photo.apply)(Photo.unapply)
  )

  def create = Action.async { implicit request =>
    directUploadForm.bindFromRequest.fold(
      formWithErrors => future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) },
      photoDetails => {
        val body = request.body.asMultipartFormData
        val resourceFile = body.get.file("image")
        if (resourceFile.isEmpty) {
          val formWithErrors = directUploadForm.withError(FormError("image", "Must supply image"))
          future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) }
        } else {
          CloudinaryResource.upload(resourceFile.get.ref.file, UploadParameters().faces(true).colors(true).imageMetadata(true).exif(true)).map {
            cr =>
              val photo = Photo(photoDetails.recipeId, cr)
              val newPhotoId = DB.withSession{ implicit session =>
//                updateImage(photo.recipeId, cr.url())
              }
              Ok(Json.obj("ok" -> true, "bilde" -> cr.url()))
          }
        }
      })
  }


  def imageForm = Action { implicit request =>
    Ok(views.html.imageform())
  }
}