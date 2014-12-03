package controllers

import com.google.inject.Inject
import myUtils.JsonFormats
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Writes, Json}
import repository.Exceptions.NoSuchRecipeException
import repository.models.{RecipeImage, User, Image}
import repository.services.RecipeService

import scala.concurrent._
import play.api.data._
import play.api.data.Forms._
import cloudinary.model.CloudinaryResource
import cloudinary.model.CloudinaryResource.preloadedFormatter
import ExecutionContext.Implicits.global
import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator


class ImageController @Inject() (
  val recipeService: RecipeService,
  implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  val directUploadForm = Form(
    mapping(
      "recipeId" -> longNumber,
      "image" -> of[CloudinaryResource]
    )(Image.apply)(Image.unapply)
  )

  def saveRecipeImage = SecuredAction.async { implicit request =>
    directUploadForm.bindFromRequest.fold(
      formWithErrors => future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) },
      imageDetails => {
        val body = request.body.asMultipartFormData
        val resourceFile = body.get.file("image")
        if (resourceFile.isEmpty) {
          val formWithErrors = directUploadForm.withError(FormError("image", "Must supply image"))
          future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) }
        } else {
          try {
            val recipeImage = recipeService.saveImage(imageDetails.recipeId, resourceFile.get.ref.file)
            recipeImage.map(i => Created(Json.obj("ok" -> true, "recipeImage" -> Json.toJson(i))))
          }
          catch {
            case e: NoSuchRecipeException => Future(BadRequest(Json.obj("ok" -> false, "message" -> e.getMessage)))
          }
        }
      })
  }

  def imageForm = SecuredAction { implicit request =>
    Ok(views.html.imageform())
  }
}