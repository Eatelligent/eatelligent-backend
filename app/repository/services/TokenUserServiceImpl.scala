package repository.services

import repository.models.TokenUser
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TokenUserServiceImpl extends TokenUserService {
  def create (token: TokenUser): Future[Option[TokenUser]] = {
    TokenUser.save(token).map(Some(_))
  }
  def retrieve (id: String): Future[Option[TokenUser]] = {
    TokenUser.findById(id)
  }
  def consume (id: String): Unit = {
    TokenUser.delete(id)
  }
}