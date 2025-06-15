package cword.view

import cword.MainApp
import cword.model.EndGameBehavior
import cword.util.MusicPlayer
import cword.util.MusicPlayer.clickButtonSoundEffect
import scalafx.scene.control.{Button, Label}
import scalafxml.core.macros.sfxml

@sfxml
class SecondLevelEndGameController(private val tryAgainSecondLevelButton: Button,
                                   private val returnButton: Button,
                                   private val numWordTyped: Label) extends EndGameBehavior{

  if (MusicPlayer.isMusicEnabled) {
    MusicPlayer.playBackgroundMusic()
  }

  numWordTyped.text = "Correct Word Typed: \n" + MainApp.gameData.last.wordCount.value.toString
  numWordTyped.style = "-fx-font-size: 22px; -fx-text-fill: #faff7f; -fx-font-family: 'Comic Sans MS'; -fx-text-alignment: left;"

  override def handleTryAgain(): Unit = {
    clickButtonSoundEffect()
    MusicPlayer.pauseBackgroundMusic()
    MainApp.startGame(2, 3500)
  }

  override def handleBackToHomepage(): Unit = {
    clickButtonSoundEffect()
    MusicPlayer.pauseBackgroundMusic()
    MainApp.showHomepage()
  }
}
