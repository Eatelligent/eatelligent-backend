package repository.services

import org.apache.commons.lang3.exception.ExceptionUtils
import play.api.Logger
import play.api.libs.mailer._
import play.api.Play.current

class MailServiceImpl extends MailService {

  def sendMailExceptionMail(exception: Throwable) = {
    val email = Email(
      "Mealchooser: Exception",
      "Mealchooser <noreply@mealchooser.com>",
      Seq("<sigurd.l@gmail.com>", "<tandeey@gmail.com>"),
      bodyText = Some("An unexpected exception has accured: " + exception.getMessage + "\n\nStacktrace:\n" +
        ExceptionUtils.getStackTrace(exception))
    )
    sendMail(email)
  }

  def forgotPassword(address: String, link: String) = {
    val email = Email(
      "Mealchooser: Reset Password",
      "Mealchooser <noreply@mealchooser.com>",
      Seq(address),
      bodyText = Some("Go here to reset your password: " + link)
    )
    sendMail(email)
  }

  def sendMail(email: Email) = {
    if (play.api.Play.isDev(play.api.Play.current)) {
      Logger.info("Not sending emails in develop mode.")
    }
    else {
      MailerPlugin.send(email)
      Logger.info("Sent mail.")
    }
  }

}
