package cword

import cword.model.GameResult
import cword.util.Database
import cword.view.GameController
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.layout.AnchorPane

import java.time.LocalDateTime

object MainApp extends JFXApp {
  Database.dropTable()
  Database.setupDB()

  val gameData = new ObservableBuffer[GameResult]()
  retrieveGameData()

  var currentLevel: Int = 1

  val rootResource = getClass.getResource("/cword/view/Homepage.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[javafx.scene.layout.AnchorPane]
  val cssResource = getClass.getResource("view/GameDeco.css")
  roots.stylesheets = List(cssResource.toExternalForm)

  stage = new PrimaryStage {
    title = "NAMANANA"
    icons += new Image(getClass.getResourceAsStream("/images/dinosaur.png"))
    scene = new Scene(roots)
  }

  def startGame(level: Int, wordSpeed: Int): Unit = {
    currentLevel = level

    val resource = getClass.getResource("/cword/view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val gameRoot = loader.getRoot[javafx.scene.layout.AnchorPane]
    val control = loader.getController[GameController#Controller]
    val cssResource = getClass.getResource("view/GameDeco.css")
    gameRoot.stylesheets = List(cssResource.toExternalForm)

    control.startSpawning(wordSpeed)

    stage.scene = new Scene(gameRoot)
    stage.centerOnScreen()
  }

  def showResults(): Unit = {
    val resource = getClass.getResource("/cword/view/ShowResults.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load
    val resultRoot = loader.getRoot[javafx.scene.layout.AnchorPane]
    resultRoot.stylesheets = List(cssResource.toExternalForm)
    stage.scene = new Scene(resultRoot)
    stage.show()
  }

  def endGame(): Unit = {
    if (currentLevel == 1) {
      firstLevelEndGame()
    } else {
      secondLevelEndGame()
    }
  }

  private def firstLevelEndGame(): Unit = {
    val resource = getClass.getResource("/cword/view/FirstLevelEndGame.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val postGameRoot = new AnchorPane(loader.getRoot[javafx.scene.layout.AnchorPane])
    postGameRoot.stylesheets = List(cssResource.toExternalForm)
    stage.scene = new Scene(postGameRoot)
  }

  private def secondLevelEndGame(): Unit = {
    val resource = getClass.getResource("/cword/view/SecondLevelEndGame.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val postGameRoot = new AnchorPane(loader.getRoot[javafx.scene.layout.AnchorPane])
    postGameRoot.stylesheets = List(cssResource.toExternalForm)
    stage.scene = new Scene(postGameRoot)
  }

  def showHomepage(): Unit = {
    val resource = getClass.getResource("/cword/view/Homepage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    val homepageRoot = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]
    stage.scene = new Scene(homepageRoot)
  }

  def showRules(): Unit = {
    val resource = getClass.getResource("/cword/view/HowToPlay.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val ruleRoot = new AnchorPane(loader.getRoot[javafx.scene.layout.AnchorPane])
    ruleRoot.stylesheets = List(cssResource.toExternalForm)
    stage.scene = new Scene(ruleRoot)
  }

  def gameEnded(): Unit = {
    endGame()
  }

  private def retrieveGameData(): Unit = {
    gameData.clear()
    gameData ++= GameResult.getResult
  }

  def saveGame(wordCount: Int): Unit = {
    val result = GameResult(LocalDateTime.now(), currentLevel, wordCount)
    result.save()
    retrieveGameData()
  }
}
