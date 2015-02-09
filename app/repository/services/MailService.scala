package repository.services

trait MailService {

  def sendMailExceptionMail(exception: Throwable)

  def forgotPassword(email: String, link: String)

}
