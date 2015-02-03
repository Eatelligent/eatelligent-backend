package myUtils

import com.github.tminglei.slickpg._
import slick.driver.PostgresDriver
import com.github.tototoshi.slick.{GenericJodaSupport, PostgresJodaSupport}

trait WithMyDriver {
  val driver: MyPostgresDriver
//  val jodaSupport: GenericJodaSupport
}

////////////////////////////////////////////////////////////
trait MyPostgresDriver extends PostgresDriver
with PgArraySupport
with PgDateSupportJoda
with PgRangeSupport
with PgSearchSupport
with PgPlayJsonSupport {

  override lazy val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  //////
  trait ImplicitsPlus extends Implicits
  with ArrayImplicits
  with DateTimeImplicits
  with RangeImplicits
  with JsonImplicits
  with SearchImplicits

  trait SimpleQLPlus extends SimpleQL
  with ImplicitsPlus
  with SearchAssistants
}

object MyPostgresDriver extends MyPostgresDriver