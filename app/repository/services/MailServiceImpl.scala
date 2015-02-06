package repository.services

import play.api.libs.mailer._
import play.api.Play.current

class MailServiceImpl extends MailService {

  def sendMail = {
    val email = Email(
      "Simple email",
      "Mister FROM <noreply@mealchooser.com>",
      Seq("Miss TO <sigurd.l@gmail.com>"),
      // sends text, HTML or both...
      bodyText = Some("A text message"),
      bodyHtml = Some("<html><body><p>An <b>html</b> message</p></body></html>")
    )
    MailerPlugin.send(email)
  }

}
