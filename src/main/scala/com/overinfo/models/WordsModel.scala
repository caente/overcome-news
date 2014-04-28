package com.overinfo.models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import spray.json._
import DefaultJsonProtocol._

/**
 * Created: Miguel A. Iglesias
 * Date: 1/25/14
 */
object WordsModel extends Persistence with Limitable {

  trait Words {


    override def hashCode(): Int = word.hashCode

    def word: String

    override def equals(obj: scala.Any): Boolean = obj match {
      case that: TweetWord => that.word == this.word && this.hashCode() == that.hashCode()
      case that: WordMerged => that.word == this.word && this.hashCode() == that.hashCode()
      case _ => false
    }
  }

  case class TweetWord(_id: String, source: SourcesModel.Source, word: String, count: Int, text: String, last_update:DateTime) extends Words

  case class WordMerged(word: String, count: Int) extends Words

  implicit val wordMerged = jsonFormat2(WordMerged)


  RegisterJodaTimeConversionHelpers()


  def getWordsSource(source: Long, limit: Option[Int] = None): List[TweetWord] = {
    db("words").find(MongoDBObject("source" -> source)).sort(MongoDBObject("history" -> -1)).limitOption(limit).map {
      dbo =>
        TweetWord(
          dbo.get("_id").toString,
          SourcesModel.getSource(source).get,
          dbo.getAs[String]("word").get,
          dbo.getAs[Int]("count").get,
          dbo.getAs[String]("text").getOrElse(""),
          dbo.getAs[DateTime]("last_update").get
        )
    }.toList
  }

  def mergeWords(merged: List[WordMerged], words: List[TweetWord]): List[WordMerged] = {
    merged.intersect(words).map {
      case WordMerged(word, count) => words.find(_.word == word).map(w => WordMerged(word, count + w.count)).get
    }
  }

  def mergeSources(sources: List[Long], limit: Option[Int] = Some(5)): List[WordMerged] = {
    val words: List[List[TweetWord]] = sources.map(source => getWordsSource(source))
    words.foldLeft(List.empty[WordMerged]) {
      (merged, word_list) =>
        if (merged.isEmpty)
          word_list.map(w => WordMerged(w.word, w.count))
        else
          mergeWords(merged, word_list)
    }.limitOption(limit)

  }


}
