package repository.services

trait MailService {

  def sendMailExceptionMail(exception: Throwable)

}
