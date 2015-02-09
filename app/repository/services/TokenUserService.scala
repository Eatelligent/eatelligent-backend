package repository.services

import repository.models.TokenUser
import com.mohiva.play.silhouette.core.services.TokenService

import scala.concurrent.Future

trait TokenUserService extends TokenService[TokenUser]{

  def create (token: TokenUser): Future[Option[TokenUser]]

  def retrieve (id: String): Future[Option[TokenUser]]

  def consume (id: String): Unit

}
