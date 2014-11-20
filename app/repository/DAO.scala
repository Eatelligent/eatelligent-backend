package repository

import play.api.db.slick.DB
import myUtils.MyPostgresDriver


class DAO(override val driver: MyPostgresDriver) extends LanguageComponent  {


}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}
