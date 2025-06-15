package cword.view

import cword.MainApp
import cword.util.MusicPlayer
import cword.util.MusicPlayer.clickButtonSoundEffect
import scalafx.animation.{Animation, ScaleTransition}
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.util.Duration

@sfxml
class HomepageController(private var background: ImageView,
                         private var title: ImageView,
                         private val dinosaur: ImageView,
                         private val startGameButton: Button,
                         private val showScoresButton: Button,
                         private val howToPlayButton: Button,
                         private var musicButton: Button) {

  MusicPlayer.playBackgroundMusic()


  val dinosaurTransition = new ScaleTransition(Duration(1000), dinosaur) {
    byX = 0.2
    byY = 0.2

    autoReverse = true
    cycleCount = Animation.Indefinite
  }

  dinosaurTransition.play()

  val titleTransition = new ScaleTransition(Duration(1000), title) {
    byX = 0.2
    byY = 0.2

    autoReverse = true
    cycleCount = Animation.Indefinite
  }

  titleTransition.play()

  def handleStartGame(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    MainApp.startGame(level = 1, wordSpeed = 7000)
    MusicPlayer.pauseBackgroundMusic()
  }

  def handleShowScores(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    MainApp.showResults()
  }

  def handleHowToPlay(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    MainApp.showRules()
  }

  def handleMusicButton(): Unit = {
    clickButtonSoundEffect()
    MusicPlayer.toggleAllMusic()
  }

}

