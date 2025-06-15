package cword.view

import cword.MainApp
import cword.util.MusicPlayer.clickButtonSoundEffect
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, TextArea}
import scalafxml.core.macros.sfxml

@sfxml
class HowToPlayController(private val back: Button,
                          private val guidelines: TextArea){

  def handleBack(event: ActionEvent): Unit = {
    clickButtonSoundEffect()
    MainApp.showHomepage()
  }
}