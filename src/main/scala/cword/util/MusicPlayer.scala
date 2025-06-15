package cword.util

import scalafx.scene.media.{Media, MediaPlayer}

object MusicPlayer{
  var musicEnabled: Boolean = true
  private var backgroundMusic: MediaPlayer = _
  private var winSoundEffect: MediaPlayer = _
  private var buttonSoundEffect: MediaPlayer = _

  val musicFile = getClass.getResource("/music/backgroundMusic.mp3")
  val media = new Media(musicFile.toString)
  backgroundMusic = new MediaPlayer(media)
  backgroundMusic.setCycleCount(MediaPlayer.Indefinite)

  def correctSoundEffect(): Unit = {
    val correctSoundEffectFile = getClass.getResource("/music/soundEffect.MP3")
    val media = new Media(correctSoundEffectFile.toString)
    winSoundEffect = new MediaPlayer(media)
    winSoundEffect.play()
  }

  def clickButtonSoundEffect(): Unit = {
    val correctSoundEffectFile = getClass.getResource("/music/buttonEffect.MP3")
    val media = new Media(correctSoundEffectFile.toString)
    buttonSoundEffect = new MediaPlayer(media)
    buttonSoundEffect.play()
  }

  def playBackgroundMusic(): Unit = {
    if (musicEnabled) {
      backgroundMusic.play()
    }
  }

  def pauseBackgroundMusic(): Unit = {
    backgroundMusic.pause()
  }

  def toggleAllMusic(): Unit = {
    musicEnabled = !musicEnabled
    if (musicEnabled) {
      playBackgroundMusic()
    } else {
      pauseBackgroundMusic()
      // Pausing other sounds currently playing (if needed)
      if (winSoundEffect != null) winSoundEffect.pause()
      if (buttonSoundEffect != null) buttonSoundEffect.pause()
    }
  }

  def isMusicEnabled: Boolean = musicEnabled
}