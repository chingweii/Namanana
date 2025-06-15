package cword.model

import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration
import scalafx.scene.control.Label

class CountdownTimer(initialTimeSeconds: Int, timerLabel: Label, onTimeUp: () => Unit) {
  private var remainingTimeMillis = initialTimeSeconds * 1000
  private val countdownTimer = new Timeline {
    cycleCount = Timeline.Indefinite
    keyFrames = Seq(
      KeyFrame(Duration(10), onFinished = _ => {
        if (remainingTimeMillis > 0) {
          remainingTimeMillis -= 10
          val seconds = remainingTimeMillis / 1000
          val milliseconds = remainingTimeMillis % 1000 / 10
          timerLabel.text = f"Time: $seconds%02d.$milliseconds%02d"
        } else {
          stop()
          onTimeUp()
        }
      })
    )
  }

  def start(): Unit = countdownTimer.play()

  def pause(): Unit = countdownTimer.pause()

  def resume(): Unit = countdownTimer.play()

  def stop(): Unit = countdownTimer.stop()

  def reset(): Unit = {
    remainingTimeMillis = initialTimeSeconds * 1000
    updateLabel()
  }

  private def updateLabel(): Unit = {
    val seconds = remainingTimeMillis / 1000
    val milliseconds = remainingTimeMillis % 1000 / 10
    timerLabel.text = f"Time: $seconds%02d.$milliseconds%02d"
  }
}