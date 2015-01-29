package dao

import org.grouplens.lenskit.data.dao.SortOrder
import org.grouplens.lenskit.data.sql.{BasicSQLStatementFactory, SQLStatementFactory}

class CusomSQLStatementFactory(
                                tableName: String,
                                userColumn: String,
                                itemColumn: String,
                                ratingColumn: String,
                                timestampColumn: String
                                ) extends BasicSQLStatementFactory {


}
