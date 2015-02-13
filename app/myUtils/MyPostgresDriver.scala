package myUtils

import com.github.tminglei.slickpg._
import slick.driver.PostgresDriver

trait MyPostgresDriver extends PostgresDriver
  with PgArraySupport
  with PgDateSupportJoda
  with PgRangeSupport
  with PgSearchSupport
  with PgPlayJsonSupport
  with PgNetSupport
  with PgLTreeSupport
  with PgHStoreSupport {

  override lazy val Implicit = new ImplicitsPlus {}
  override val simple = new SimpleQLPlus {}

  //////
  trait ImplicitsPlus extends Implicits
  with ArrayImplicits
  with DateTimeImplicits
  with RangeImplicits
  with JsonImplicits
  with SearchImplicits
  with SimpleHStorePlainImplicits
  with SimpleNetPlainImplicits
  with SimpleLTreePlainImplicits
  with SimpleArrayPlainImplicits

  trait SimpleQLPlus extends SimpleQL
  with ImplicitsPlus
  with SearchAssistants
}

object MyPostgresDriver extends MyPostgresDriver