package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import myUtils.JsonFormats
import repository.daos.FavoritesDAO
import repository.models.User
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import myUtils.JsonFormats

import scala.concurrent.Future

class FavoritesController @Inject() (
                                      val favoritesDAO: FavoritesDAO, 
                                      implicit val env: Environment[User, CachedCookieAuthenticator]) 
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {
  
  def listFavorites = SecuredAction.async { implicit request =>
    val favorites = favoritesDAO.getFavoritesForUser(request.identity.userID.get)
    favorites.map(f => Ok(Json.obj("ok" -> true, "favorites" -> Json.toJson(f))))
  }

  def addToFavorite(recipeId: Long) = SecuredAction.async { implicit request =>
    val fav = favoritesDAO.addToFavorites(request.identity.userID.get, recipeId)
    fav.map(f => Ok(Json.obj("ok" -> true, "favorites" -> Json.toJson(f))))
  }

  def removeFromFavorite(recipeId: Long) = SecuredAction.async { implicit request =>
    favoritesDAO.removeFromFavorites(request.identity.userID.get, recipeId)
    Future.successful( Ok(Json.obj("ok" -> true, "message" -> "Removed from favorite")))
  }

}
