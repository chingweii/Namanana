package cword.view

import cword.MainApp
import cword.model.GameResult
import cword.util.MusicPlayer.clickButtonSoundEffect
import scalafx.scene.control.{Button, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import scalafx.beans.property.StringProperty
import scalafx.event.ActionEvent

import java.time.format.DateTimeFormatter

@sfxml
class ShowResultsController(private val backButton: Button,
                            private val resultTable: TableView[GameResult],
                            private val timeColumn: TableColumn[GameResult, String],
                            private val levelColumn: TableColumn[GameResult, String],
                            private val correctWordCountColumn: TableColumn[GameResult, String]) {

  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  initialize()

  def initialize(): Unit = {
    resultTable.items = MainApp.gameData

    timeColumn.cellValueFactory = { data =>
      StringProperty(data.value.gameTime.value.format(dateFormatter))
    }

    levelColumn.cellValueFactory = { data =>
      StringProperty(data.value.level.value.toString)
    }

    correctWordCountColumn.cellValueFactory = { data =>
      StringProperty(data.value.wordCount.value.toString)
    }
  }

  def handleBack(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    MainApp.showHomepage()
  }
}
