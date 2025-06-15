package cword.model

import java.time.LocalDateTime
import scalikejdbc._
import cword.util.Database
import scalafx.beans.property.{IntegerProperty, ObjectProperty}
import scala.util.Try

class GameResult(dateD: LocalDateTime, levelI: Int, wordCountI: Int)extends Database {

  val gameTime = ObjectProperty[LocalDateTime](dateD)
  val level = IntegerProperty(levelI)
  val wordCount = IntegerProperty(wordCountI)

  def save(): Try[Int] = {
    Try(DB.autoCommit { implicit session =>
      sql"""
        insert into gameResult (gameTime, level, wordCount) values
        (${gameTime.value.toString}, ${level.value}, ${wordCount.value})
      """.update.apply()
    })
  }
}

object GameResult extends Database {
  def apply (gameTime: LocalDateTime, level: Int, wordCount: Int) : GameResult = {
    new GameResult(gameTime, level, wordCount)
  }

  def initializeTable(): Boolean = {
    DB autoCommit { implicit session =>
      sql"""
        create table gameResult (
          id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          gameTime varchar(64),
          level int not null,
          wordCount int not null
        )
      """.execute.apply()
    }
  }

  def getResult : List[GameResult] = {
    DB readOnly { implicit session =>
      sql"select * from gameResult".map(rs =>
        GameResult(
          LocalDateTime.parse(rs.string("gameTime")),
          rs.int("level"),
          rs.int("wordCount")
        )).list.apply()
    }
  }
}

