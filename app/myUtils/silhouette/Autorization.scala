package myUtils.silhouette

import com.mohiva.play.silhouette.core.Authorization
import models.{User}
import play.api.mvc.RequestHeader
import play.api.i18n.Lang


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
