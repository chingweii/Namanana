package cword.view

import cword.MainApp
import cword.model.{CountdownTimer, Game}
import cword.util.MusicPlayer.clickButtonSoundEffect
import scalafxml.core.macros.sfxml
import scalafx.scene.layout.{AnchorPane, VBox}
import scalafx.scene.image.ImageView
import scalafx.util.Duration
import scalafx.animation.{KeyFrame, Timeline, TranslateTransition}
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.Scene
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafx.stage.{Modality, Stage, StageStyle}

@sfxml
class GameController(private val root: AnchorPane,
                     private val dinosaur: ImageView,
                     private var background: MediaView) {

  private val game = new Game(root)
  private var dinosaurTransition: TranslateTransition = _
  private val timerLabel = new Label("Time: 30:00") {
    style = "-fx-font-size: 30px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #685634;"
  }

  private var mediaPlayer: MediaPlayer = _

  initialize()

  def initialize(): Unit = {
    val videoFile = getClass.getResource("/videos/desert.mp4").toString
    val media = new Media(videoFile)
    mediaPlayer = new MediaPlayer(media)

    background.setMediaPlayer(mediaPlayer)
    background.setPreserveRatio(true)

    mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
    mediaPlayer.play()
  }


  root.children.add(timerLabel)
  AnchorPane.setTopAnchor(timerLabel, 10.0)
  AnchorPane.setLeftAnchor(timerLabel, 720.0)

  private val countdownTimer = new CountdownTimer(30, timerLabel, () => endGame())

  def startSpawning(wordSpeed: Int): Unit = {
    game.wordSpeed = wordSpeed
    val initialDelay = Duration(500)

    val timeline = new Timeline {
      cycleCount = 1
      keyFrames = Seq(
        KeyFrame(initialDelay, onFinished = _ => game.spawnWord())
      )
    }

    timeline.play()
    startDinosaurOscillatingMovement()
    countdownTimer.start()
  }

  // Move dinosaur back and forth
  def startDinosaurOscillatingMovement(): Unit = {
    dinosaurTransition = new TranslateTransition(Duration(300), dinosaur) {
      fromX = 0
      toX = 10
      cycleCount = TranslateTransition.Indefinite
      autoReverse = true
    }

    dinosaurTransition.play()
  }

  root.onKeyTyped = event => {
    val character = event.getCharacter
    if (game.handleKeyInput(character)) {
    } else if (game.getTypedWord.isEmpty) {

    }
  }

  private var isPaused = false

  def handlePause(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    game.pauseGame()
    dinosaurTransition.stop()
    countdownTimer.pause()
    mediaPlayer.pause()
    showPopup()
  }

  private def showPopup(): Unit = {
    lazy val popupStage: Stage = new Stage {
      initModality(Modality.ApplicationModal)
      initStyle(StageStyle.UNDECORATED)
      scene = new Scene {
        root = new VBox {
          spacing = 10
          padding = Insets(20)
          minWidth = 75
          minHeight = 30
          alignment = Pos.Center

          style = """
            -fx-background-color: #8b5d33;
            -fx-border-color: #685634;
            -fx-border-width: 4px;
          """
          children = Seq(
            new Button("Resume") {
              style = "-fx-font-size: 30px; -fx-text-fill: #faff7f; -fx-background-color: #91785d; -fx-padding: 5px 10px; -fx-border-width: 10px, border-radius: 10px"
              onAction = _ => {
                clickButtonSoundEffect()
                popupStage.close()
                dinosaurTransition.play()
                mediaPlayer.play()
                countdownTimer.resume()
                game.resumeGame()
              }
            },
            new Button("Exit") {
              style = "-fx-font-size: 30px; -fx-text-fill: #faff7f; -fx-background-color: #91785d; -fx-padding: 5px 10px; -fx-border-width: 10px, border-radius: 10px"
              onAction = _ => {
                clickButtonSoundEffect()
                popupStage.close()
                MainApp.showHomepage()
              }
            }
          )
        }
      }
    }

    popupStage.showAndWait()
  }

  def endGame(): Unit = {
    game.endGame()
    MainApp.gameEnded()
  }

}
