package cword.util

import cword.model.GameResult
import scalikejdbc._
import scala.util.{Try, Failure}
import java.sql.SQLException

trait Database {
  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"

  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")

  implicit val session: DBSession = AutoSession
}

object Database extends Database {
  def setupDB(): Unit = {
    if (!hasDBInitialized) {
      GameResult.initializeTable()
    }
  }

  // Check if the database table has been initialized
  def hasDBInitialized: Boolean = {
    DB getTable "gameResult" match {
      case Some(x) => true
      case None => false
    }
  }


  def dropTable(): Unit = {
    DB.autoCommit { implicit session =>
      // Try creating the schema (ignore if it already exists)
      Try {
        sql"CREATE SCHEMA ME".execute.apply()
      } match {
        case Failure(e: SQLException) if e.getSQLState == "X0Y68" =>
        // Schema already exists
        case Failure(e) =>
          throw e
        case _ =>
      }

      // Try dropping the table (ignore if it doesn't exist)
      Try {
        sql"DROP TABLE ME.gameResult".execute.apply()
      } match {
        case Failure(e: SQLException) if e.getSQLState == "42Y55" =>
        // Table doesn't exist
        case Failure(e) =>
          throw e
        case _ =>
      }
    }
  }
}
