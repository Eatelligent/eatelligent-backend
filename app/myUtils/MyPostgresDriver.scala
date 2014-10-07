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
with PgHStoreSupport
with PgPlayJsonSupport
with PgSearchSupport
with PgPostGISSupport {

  override lazy val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  //////
  trait ImplicitsPlus extends Implicits
  with ArrayImplicits
  with DateTimeImplicits
  with RangeImplicits
  with HStoreImplicits
  with JsonImplicits
  with SearchImplicits
  with PostGISImplicits

  trait SimpleQLPlus extends SimpleQL
  with ImplicitsPlus
  with SearchAssistants
  with PostGISAssistants
}

object MyPostgresDriver extends MyPostgresDriver