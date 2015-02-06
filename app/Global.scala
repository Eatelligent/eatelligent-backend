import myUtils.CorsFilter
import play.api.i18n.Lang
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.{Application, GlobalSettings}
import play.api.mvc.{WithFilters, Result, RequestHeader}
import com.mohiva.play.silhouette.core.SecuredSettings
import repository.exceptions._
import myUtils.di.SilhouetteModule
import repository.services.{MailServiceImpl, MailService}
import scala.concurrent.Future
import com.google.inject.{Inject, Guice, Injector}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * The global configuration.
 */
object Global extends WithFilters(new CorsFilter) with GlobalSettings with SecuredSettings {

  /**
   * The Guice dependencies injector.
   */
  var injector: Injector = _

  @Inject
  val mailService: MailService = new MailServiceImpl
  
  override def onStart(app: Application) = {
    super.onStart(app)
    // Now the configuration is read and we can create our Injector.
    injector = Guice.createInjector(new SilhouetteModule())
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future {
      ex.getCause match {
        case e: IllegalArgumentException => BadRequest(Json.obj("ok" -> false, "message" -> Json
          .toJson(ex.getMessage)))
        case e: NoSuchRecipeException => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson(ex.getMessage)))
        case e: NoSuchIngredientException => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson(ex.getMessage)))
        case e: DuplicateException => Conflict(Json.obj("ok" -> false, "message" -> Json.toJson(ex.getMessage)))
        case e: NoSuchFavoriteFoundException => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson(ex
          .getMessage)))
        case e: NoSuchUserException => NotFound(Json.obj("ok" -> false, "message" -> Json.toJson(ex.getMessage)))
        case _ =>
          mailService.sendMailExceptionMail(ex)
          InternalServerError(Json.obj("ok" -> false, "message" -> ex.getMessage))
      }
    }
  }

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(
      NotFound(Json.obj("ok" -> false, "message" -> Json.toJson("No route was found for " + request.uri)))
    )
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(
      BadRequest(Json.obj("ok" -> false, "message" -> Json.toJson("Bad Request: " + error)))
    )
  }

  /**
   * Loads the controller classes with the Guice injector,
   * in order to be able to inject dependencies directly into the controller.
   *
   * @param controllerClass The controller class to instantiate.
   * @return The instance of the controller class.
   * @throws Exception if the controller couldn't be instantiated.
   */
  override def getControllerInstance[A](controllerClass: Class[A]) = injector.getInstance(controllerClass)

  /**
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @param lang The currently selected language.
   * @return The result to send to the client.
   */
  override def onNotAuthenticated(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future.successful(Unauthorized(Json.obj("ok" -> false, "message" -> "Not authenticated"))))
  }

  /**
   * Called when a user is authenticated but not authorized.
   *
   * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
   *
   * @param request The request header.
   * @param lang The currently selected language.
   * @return The result to send to the client.
   */
  override def onNotAuthorized(request: RequestHeader, lang: Lang): Option[Future[Result]] = {
    Some(Future.successful(Forbidden(Json.obj("ok" -> false, "message" -> "Authenticated, but not authorized"))))
  }
}
