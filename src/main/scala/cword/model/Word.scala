package cword.model

import scala.io.Source
import scala.util.Random

case class Word() {
  var word: String = _

  // Get a random word from word.txt
  def randomWord(): Unit = {
    val filePath = "src/main/resources/word.txt"
    val source = Source.fromFile(filePath)
    val words = source.getLines().toSeq
    word = words(Random.nextInt(words.length))
    source.close()
  }

}