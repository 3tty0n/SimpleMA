package MA

import java.io.{FileReader, PrintWriter, Reader}
import java.util.Date
import org.apache.lucene.analysis.ja.tokenattributes.{BaseFormAttribute, PartOfSpeechAttribute}
import resource._
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

/**
  * Created by micchon1 on 2016/06/08.
  */
object BulkMA extends App {

  val REFERENCE_TERM = List(
    "これ", "ここ", "こいつ", "こっち", "この", "こう", "こんな",
    "それ",	"そこ",	"そっち", "そちら",  "そいつ", "そなた",	"その"	, "そう", "そんな",
    "あれ", 	"あそこ", 	"あっち", "あちら", "あいつ", "あなた", "あの", "ああ", "あんな",
    "どれ", "どこ", "どっち", "どちら", "どいつ", "どなた", "どの", "どう", "どんな"
  )

  val FORMAT_NOUN = List(
    "こと", "もの", "ところ", "わけ", "はず", "つもり", "ため", "せい", "おかげ",
    "まま", "とおり", "かわり", "くせ", "わり", "ほう", "たび", "よう", "さん", "うち",
    "もん", "いつ", "なに"
  )

  val FILTER_TERM = REFERENCE_TERM ++ FORMAT_NOUN

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

  def duplicateAggregate(lst: List[String]): List[(String, Int)] = {
    lst.groupBy(identity).mapValues(_.size).toList.sortWith(_._2 > _._2)
  }

  def writeToFile(lst: List[(String, Int)]): Unit = {
    val date = new Date()
    val file = new PrintWriter(s"output/${date.toString}")
    lst.foreach { element =>
      file.write(s"$element \n")
    }
    file.close()
  }

  for (input <- managed(new FileReader("data/neko.txt"))) {
    val noun = tokenize(input).withFilter(token => token("pos") == "名詞").map(token => token("surface"))
    val rankedList = duplicateAggregate(noun.toList)
      .filterNot(p => FILTER_TERM.contains(p._1) )
      .filter(p => p._1.size > 1)
      .take(50)
    rankedList foreach println
    //writeToFile(rankedList)
  }


}
