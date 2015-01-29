package recommandation.recommendation

import java.sql.Connection

import lenskit.Setup

class Recommandation(conn: Connection) {

  def ai(userId: Long) = {
    Setup.run(userId, conn)
  }

}
