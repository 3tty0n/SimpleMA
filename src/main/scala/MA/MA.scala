package MA

import java.io.{File, FileReader}
import scala.io.Source

import org.apache.lucene.analysis.ja.tokenattributes.{PartOfSpeechAttribute, BaseFormAttribute}
import resources._
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.atilika.kuromoji.Tokenizer
import org.atilika.kuromoji.Token

/**
  * Created by micchon1 on 2016/06/08.
  */
object MA extends App {

  val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build()
  val tokens = tokenizer.tokenize("私のアツいアイドル活動、アイカツ！始まります！フフッヒ。").toArray

  def outToken(tokens: Array[AnyRef]): Unit = {
    tokens foreach { t =>
      val token = t.asInstanceOf[Token]
      println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
    }

    println("-------------------------------------------")

    tokens foreach { t =>
      val token = t.asInstanceOf[Token]
      if (token.getPartOfSpeech.startsWith("名詞"))
        println(s"${token.getSurfaceForm} - ${token.getAllFeatures}")
    }
  }

  outToken(tokens)
}
