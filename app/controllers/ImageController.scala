package controllers

import com.google.inject.Inject
import play.api.libs.json.{JsError, Json}
import repository.services.RecipeService
import repository.{Image, RecipeImage}

import scala.concurrent._
import play.api.mvc.Action
import play.api.data._
import play.api.data.Forms._
import cloudinary.model.CloudinaryResource
import cloudinary.model.CloudinaryResource.preloadedFormatter
import ExecutionContext.Implicits.global


class ImageController @Inject() (
  val recipeService: RecipeService
                                  ) extends MyController {


  val directUploadForm = Form(
    mapping(
      "recipeId" -> longNumber,
      "image" -> of[CloudinaryResource]
    )(Image.apply)(Image.unapply)
  )

  def saveRecipeImage = Action.async { implicit request =>
    directUploadForm.bindFromRequest.fold(
      formWithErrors => future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) },
      imageDetails => {
        val body = request.body.asMultipartFormData
        val resourceFile = body.get.file("image")
        if (resourceFile.isEmpty) {
          val formWithErrors = directUploadForm.withError(FormError("image", "Must supply image"))
          future { BadRequest(Json.obj("ok" -> false, "message" -> formWithErrors.errorsAsJson)) }
        } else {
          val recipeImage = recipeService.saveImage(imageDetails.recipeId, resourceFile.get.ref.file)
          recipeImage.map(i => Ok(Json.obj("ok" -> true, "recipeImage" -> Json.toJson(i))))
        }
      })
  }


  def imageForm = Action { implicit request =>
    Ok(views.html.imageform())
  }
}