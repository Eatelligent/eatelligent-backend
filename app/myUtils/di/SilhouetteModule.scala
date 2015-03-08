package myUtils.di

import play.api.Play
import play.api.Play.current
import com.google.inject.{ Provides, AbstractModule }
import net.codingwell.scalaguice.ScalaModule
import com.mohiva.play.silhouette.core.{EventBus, Environment}
import com.mohiva.play.silhouette.core.utils._
import com.mohiva.play.silhouette.core.services._
import com.mohiva.play.silhouette.core.providers._
import com.mohiva.play.silhouette.core.providers.oauth2._
import com.mohiva.play.silhouette.contrib.utils._
import com.mohiva.play.silhouette.contrib.services._
import com.mohiva.play.silhouette.contrib.daos.DelegableAuthInfoDAO
import recommandation.coldstart.{UserColdStartImpl, UserColdStart}
import repository.services.{UserService, UserServiceImpl}
import repository.daos._
import repository.daos.slick._
import repository.models.User
import repository.services._

/**
 * The Guice module which wires all Silhouette dependencies.
 */
class SilhouetteModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  def configure() {
    bind[UserService].to[UserServiceImpl]
    bind[RecipeService].to[RecipeServiceImpl]
    bind[MailService].to[MailServiceImpl]
    bind[TokenUserService].to[TokenUserServiceImpl]
    bind[UserDAO].to[UserDAOSlick]
    bind[RecipeDAO].to[RecipeDAOSlick]
    bind[TagDAO].to[TagDAOSlick]
    bind[IngredientDAO].to[IngredientDAOSlick]
    bind[LanguageDAO].to[LanguageDAOSlick]
    bind[RatingDAO].to[RatingDAOSlick]
    bind[StatsDAO].to[StatsDAOSlick]
    bind[FavoritesDAO].to[FavoritesDAOSlick]
    bind[DelegableAuthInfoDAO[PasswordInfo]].to[PasswordInfoDAOSlick]
    bind[DelegableAuthInfoDAO[OAuth1Info]].to[OAuth1InfoDAOSlick]
    bind[DelegableAuthInfoDAO[OAuth2Info]].to[OAuth2InfoDAOSlick]
    bind[UserViewedRecipeDAO].to[UserViewedRecipeDAOSlick]
    bind[ColdStartDAO].to[ColdStartDAOSlick]
    bind[UserYesNoRecipeDAO].to[UserYesNoRecipeDAOSlick]


    // AI
    bind[UserColdStart].to[UserColdStartImpl]
    bind[RecommendationDAO].to[RecommendationDAOSlick]
    bind[RecommendationService].to[RecommendationServiceImpl]

    bind[CacheLayer].to[PlayCacheLayer]
    bind[HTTPLayer].to[PlayHTTPLayer]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[EventBus].toInstance(EventBus())
  }

  /**
   * Provides the Silhouette environment.
   *
   * @param userService The user service implementation.
   * @param authenticatorService The authentication service implementation.
   * @param eventBus The event bus instance.
   * @return The Silhouette environment.
   */
  @Provides
  def provideEnvironment(
    userService: UserService,
    authenticatorService: AuthenticatorService[CachedCookieAuthenticator],
    eventBus: EventBus,
    credentialsProvider: CredentialsProvider,
    facebookProvider: FacebookProvider,
    googleProvider: GoogleProvider): Environment[User, CachedCookieAuthenticator] = {

    Environment[User, CachedCookieAuthenticator](
      userService,
      authenticatorService,
      Map(
        credentialsProvider.id -> credentialsProvider,
        facebookProvider.id -> facebookProvider,
        googleProvider.id -> googleProvider
      ),
      eventBus
    )
  }

  /**
   * Provides the authenticator service.
   *
   * @param cacheLayer The cache layer implementation.
   * @param idGenerator The ID generator used to create the authenticator ID.
   * @return The authenticator service.
   */
  @Provides
  def provideAuthenticatorService(
    cacheLayer: CacheLayer,
    idGenerator: IDGenerator): AuthenticatorService[CachedCookieAuthenticator] = {

    new CachedCookieAuthenticatorService(CachedCookieAuthenticatorSettings(
      cookieName = Play.configuration.getString("silhouette.authenticator.cookieName").get,
      cookiePath = Play.configuration.getString("silhouette.authenticator.cookiePath").get,
      cookieDomain = Play.configuration.getString("silhouette.authenticator.cookieDomain"),
      secureCookie = Play.configuration.getBoolean("silhouette.authenticator.secureCookie").get,
      httpOnlyCookie = Play.configuration.getBoolean("silhouette.authenticator.httpOnlyCookie").get,
      cookieIdleTimeout = Play.configuration.getInt("silhouette.authenticator.cookieIdleTimeout").get,
      cookieAbsoluteTimeout = Play.configuration.getInt("silhouette.authenticator.cookieAbsoluteTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").get
    ), cacheLayer, idGenerator, Clock())
  }

  /**
   * Provides the auth info service.
   *
   * @param passwordInfoDAO The implementation of the delegable password auth info DAO.
   * @param oauth1InfoDAO The implementation of the delegable OAuth1 auth info DAO.
   * @param oauth2InfoDAO The implementation of the delegable OAuth2 auth info DAO.
   * @return The auth info service instance.
   */
  @Provides
  def provideAuthInfoService(
    passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo],
    oauth1InfoDAO: DelegableAuthInfoDAO[OAuth1Info],
    oauth2InfoDAO: DelegableAuthInfoDAO[OAuth2Info]): AuthInfoService = {

    new DelegableAuthInfoService(passwordInfoDAO, oauth1InfoDAO, oauth2InfoDAO)
  }

  /**
   * Provides the avatar service.
   *
   * @param httpLayer The HTTP layer implementation.
   * @return The avatar service implementation.
   */
  @Provides
  def provideAvatarService(httpLayer: HTTPLayer): AvatarService = new GravatarService(httpLayer)

  /**
   * Provides the credentials provider.
   *
   * @param authInfoService The auth info service implemenetation.
   * @param passwordHasher The default password hasher implementation.
   * @return The credentials provider.
   */
  @Provides
  def provideCredentialsProvider(
    authInfoService: AuthInfoService,
    passwordHasher: PasswordHasher): CredentialsProvider = {

    new CredentialsProvider(authInfoService, passwordHasher, Seq(passwordHasher))
  }

  /**
   * Provides the Facebook provider.
   *
   * @param cacheLayer The cache layer implementation.
   * @param httpLayer The HTTP layer implementation.
   * @return The Facebook provider.
   */
  @Provides
  def provideFacebookProvider(cacheLayer: CacheLayer, httpLayer: HTTPLayer): FacebookProvider = {
    FacebookProvider(cacheLayer, httpLayer, OAuth2Settings(
      authorizationURL = Play.configuration.getString("silhouette.facebook.authorizationURL").get,
      accessTokenURL = Play.configuration.getString("silhouette.facebook.accessTokenURL").get,
      redirectURL = Play.configuration.getString("silhouette.facebook.redirectURL").get,
      clientID = Play.configuration.getString("silhouette.facebook.clientID").get,
      clientSecret = Play.configuration.getString("silhouette.facebook.clientSecret").get,
      scope = Play.configuration.getString("silhouette.facebook.scope")))
  }

  /**
   * Provides the Google provider.
   *
   * @param cacheLayer The cache layer implementation.
   * @param httpLayer The HTTP layer implementation.
   * @return The Google provider.
   */
  @Provides
  def provideGoogleProvider(cacheLayer: CacheLayer, httpLayer: HTTPLayer): GoogleProvider = {
    GoogleProvider(cacheLayer, httpLayer, OAuth2Settings(
      authorizationURL = Play.configuration.getString("silhouette.google.authorizationURL").get,
      accessTokenURL = Play.configuration.getString("silhouette.google.accessTokenURL").get,
      redirectURL = Play.configuration.getString("silhouette.google.redirectURL").get,
      clientID = Play.configuration.getString("silhouette.google.clientID").get,
      clientSecret = Play.configuration.getString("silhouette.google.clientSecret").get,
      scope = Play.configuration.getString("silhouette.google.scope")))
  }

}
