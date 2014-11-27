package repository.models

import cloudinary.model.CloudinaryResource
import com.cloudinary.Implicits._
import com.cloudinary.Transformation

case class Image(recipeId:Long, image:CloudinaryResource)

case class RecipeImage(recipeId:Long, url: String) {
}

case class IngredientImage(ingredientId:Long, image:CloudinaryResource) {
  def url = image.url()
  def thumbnailUrl = image.url(Transformation().w_(150).h_(150).c_("fit").quality(80))
}
