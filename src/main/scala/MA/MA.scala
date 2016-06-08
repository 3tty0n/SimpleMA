package MA

import scala.io.Source
import org.atilika.kuromoji.Tokenizer
import org.atilika.kuromoji.Token

/**
  * Created by micchon1 on 2016/06/08.
  */
object MA extends App {

  val file = Source.fromFile("data/aikatsu.txt").getLines().toList.head
  val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build()
  val tokens = tokenizer.tokenize(file).toArray

  def outToken(tokens: Array[AnyRef]): Unit = {
    tokens foreach (t => outAllFeatures(t))

    println("---------------------------------------------")

    tokens foreach (t => outNounFeatures(t))
  }

  def outAllFeatures(t: AnyRef): Unit = {
    val token = t.asInstanceOf[Token]
    println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
  }

  def outNounFeatures(t: AnyRef): Unit = {
    val token = t.asInstanceOf[Token]
    if (token.getPartOfSpeech.startsWith("名詞"))
      println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
  }

  outToken(tokens)
}
