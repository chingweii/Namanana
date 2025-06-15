package cword.model

import cword.MainApp
import cword.util.MusicPlayer.correctSoundEffect
import scalafx.animation.{Animation, TranslateTransition}
import scalafx.scene.layout.AnchorPane
import scalafx.scene.text.{Text, TextFlow}
import scalafx.util.Duration

import scala.collection.mutable.ListBuffer

class Game(private val root: AnchorPane) {
  private var currentWord: Word = new Word()
  private var typedWord: String = ""
  private var wordLabel: TextFlow = _
  private var transition: TranslateTransition = _
  private val activeTransitions = ListBuffer[Animation]()
  var correctWordCount: Int = 0
  var wordSpeed: Int = 7000

  def spawnWord(): Unit = {
    currentWord.randomWord()
    typedWord = ""

    wordLabel = new TextFlow{
      val wordText = new Text(currentWord.word) {
        style = "-fx-font-size: 40px; -fx-font-weight: 900; -fx-font-family: 'Courier New'; -fx-fill: white;"
      }
      children.add(wordText)
    }

    val spawnHeight = 120
    wordLabel.layoutY = spawnHeight

    root.children.add(wordLabel)
    root.layout()

    val windowWidth = root.width.value
    val wordWidth = wordLabel.width.value

    transition = new TranslateTransition {
      node = wordLabel
      duration = Duration(wordSpeed)
      fromX = windowWidth
      toX = -wordWidth - 300
      cycleCount = 1
      autoReverse = false
    }

    activeTransitions += transition

    transition.setOnFinished { _ =>
      root.children.remove(wordLabel)
        spawnWord()
    }

    transition.play()
  }

  def handleKeyInput(character: String): Boolean = {
    if (typedWord.length < currentWord.word.length && currentWord.word.startsWith(typedWord + character)) {
      typedWord += character

      val typedPart = new Text(typedWord) {
        style = "-fx-font-size: 40px; -fx-font-weight: 900; -fx-font-family: 'Courier New'; -fx-fill: #295135;"
      }
      val remainingPart = new Text(currentWord.word.substring(typedWord.length)) {
        style = "-fx-font-size: 40px; -fx-font-weight: 900; -fx-font-family: 'Courier New'; -fx-fill: white;"
      }

      wordLabel.children.clear()
      wordLabel.children.addAll(typedPart, remainingPart)

      if (typedWord == currentWord.word) {
        correctWordCount += 1
        correctSoundEffect()

        root.children.remove(wordLabel)
        transition.stop()
        spawnWord()
        return true
      }
    }
    false
  }

  def pauseGame(): Unit = {
    activeTransitions.foreach(_.pause())
  }

  def resumeGame(): Unit = {
    activeTransitions.foreach(_.play())
  }

  def getTypedWord: String = typedWord

  def endGame(): Unit = {
    MainApp.saveGame(correctWordCount)

  }
}


