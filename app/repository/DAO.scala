package repository

import play.api.db.slick.DB
import myUtils.MyPostgresDriver


class DAO(override val driver: MyPostgresDriver) extends LanguageComponent with RecipeComponent with
                                                          IngredientComponent with TagComponent {


}

object current {
  val dao = new DAO(DB(play.api.Play.current).driver.asInstanceOf[MyPostgresDriver])
}
