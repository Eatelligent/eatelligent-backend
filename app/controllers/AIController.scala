package controllers

import com.mohiva.play.silhouette.core.{Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import myUtils.JsonFormats
import play.api.libs.json.Json
import recommandation.recommendation.Recommandation
import repository.models.{Recommendation, Recipe, User}
import repository.services.RecipeService
import scala.concurrent.Future
import javax.inject.Inject
import scala.collection.JavaConversions._
import scala.util.{Try, Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.db._
import play.api.Play.current

class AIController @Inject() (
                               val recipeService: RecipeService,
                               implicit val env: Environment[User, CachedCookieAuthenticator])
  extends Silhouette[User, CachedCookieAuthenticator] with JsonFormats {

  def ai = SecuredAction.async { implicit request =>
    val conn: java.sql.Connection = DB.getConnection()
    val rec = new Recommandation(conn)
    val recs = rec.ai(request.identity.userID.get)
    val scores = recs.map(x => x.getId -> x.getScore).toMap

    val fRecipes = recs.map {
      r =>
        recipeService.find(r.getId, request.identity.userID.get)
    }.toList

    val listOfTrys = fRecipes.map(futureToFutureTry(_))
    val futures = Future.sequence(listOfTrys)
    val recipes = futures.map(_.collect{ case Success(x) => x})

    recipes.map(rs =>
      Ok(Json.obj("ok" -> true, "recommendations" -> Json.toJson(
        rs.flatten.map(
          x => Recommendation(scores.getOrElse(x.id.get, -1), x)
        )
    ))))
  }

  def futureToFutureTry[T](f: Future[T]): Future[Try[T]] =
    f.map(Success(_)).recover({case x => Failure(x)})

}
