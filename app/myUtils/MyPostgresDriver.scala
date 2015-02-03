package myUtils

import com.github.tminglei.slickpg._
import slick.driver.PostgresDriver

trait WithMyDriver {
  val driver: MyPostgresDriver
}

trait MyPostgresDriver extends PostgresDriver
  with PgArraySupport
  with PgDateSupportJoda
  with PgRangeSupport
  with PgSearchSupport
  with PgPlayJsonSupport {

  override val simple = new SimpleQLPlus {}

  trait SimpleQLPlus extends SimpleQL
    with SearchAssistants
    with ArrayImplicits
    with DateTimeImplicits
    with RangeImplicits
    with JsonImplicits
    with SearchImplicits
}

object MyPostgresDriver extends MyPostgresDriver