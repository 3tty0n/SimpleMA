package MA

import java.io.{Reader, FileReader}

import org.apache.lucene.analysis.ja.tokenattributes.{PartOfSpeechAttribute, BaseFormAttribute}
import resource._
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

import org.atilika.kuromoji.Tokenizer
import org.atilika.kuromoji.Token

/**
  * Created by micchon1 on 2016/06/08.
  */
object BulkMA extends App {

  def tokenize(input: Reader): Stream[Map[String, String]] = {
    val tokenizer = new JapaneseTokenizer(null, false, JapaneseTokenizer.Mode.NORMAL)
    tokenizer.setReader(input)
    tokenizer.reset()

    val charAtt = tokenizer.getAttribute(classOf[CharTermAttribute])
    val baseAtt = tokenizer.getAttribute(classOf[BaseFormAttribute])
    val posAtt = tokenizer.getAttribute(classOf[PartOfSpeechAttribute])

    def aux(): Stream[Map[String, String]] = {
      if (!tokenizer.incrementToken()) Stream.empty
      else {
        val posAry = posAtt.getPartOfSpeech.split("-")
        val token = Map(
          "surface" -> charAtt.toString,
          "base" -> (if (baseAtt.getBaseForm != null) baseAtt.getBaseForm else charAtt.toString),
          "pos" -> posAry.head
        )
        token #:: aux()
      }
    }
    aux()
  }



  for (input <- managed(new FileReader("data/neko.txt"))) {
    val noun = tokenize(input).filter(token => token("pos") == "名詞").map(token => token("surface"))
    noun foreach println
  }


}
