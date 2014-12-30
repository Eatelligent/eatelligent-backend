package myUtils.silhouette

import com.mohiva.play.silhouette.core.Authorization
import play.api.mvc.RequestHeader
import play.api.i18n.Lang
import repository.models.User


/**
Only allows those managers that have at least a role of the selected.
	Master role is always allowed.
	Ex: WithRole("high", "sales") => only managers with roles "high" or "sales" (or "master") are allowed.
  */
case class WithRole (anyOf: String) extends Authorization[User] {
  def isAuthorized (user: User)(implicit request: RequestHeader, lang: Lang) = WithRole.isAuthorized(user,
    anyOf)
}
object WithRole {
  def isAuthorized (user: User, anyOf: String): Boolean = user.role.get == anyOf
}

case class IsAuthor (authorId: Long) extends Authorization[User] {
  def isAuthorized(user: User)(implicit request: RequestHeader, lang: Lang) = IsAuthor.isAuthorized(user, authorId)
}

object IsAuthor {
  def isAuthorized(user: User, authorId: Long): Boolean = user.userID.get == authorId
}
