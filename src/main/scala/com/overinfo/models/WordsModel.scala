package com.overinfo.models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import rx.lang.scala.Observable

/**
 * Created: Miguel A. Iglesias
 * Date: 1/25/14
 */
object WordsModel {

  case class TweetWord(_id: String, source: SourcesModel.Source, word: String, count: Int, text: String, history: List[DateTime])

  case class WordMerged(word: String, count: Int)


  RegisterJodaTimeConversionHelpers()

  def getWordsSource(source: Long, limit: Int): List[TweetWord] = {
    db("words").find(MongoDBObject("source" -> source)).sort(MongoDBObject("last_update" -> 1)).limit(limit).map {
      dbo =>
        TweetWord(
          dbo.get("_id").toString,
          SourcesModel.getSource(source).get,
          dbo.getAs[String]("word").get,
          dbo.getAs[Int]("count").get,
          dbo.getAs[String]("word").get,
          dbo.getAs[List[DateTime]]("history").get
        )
    }.toList
  }

  def mergeWords(sources: List[Long], limit: Int) = {
    val words: List[List[TweetWord]] = sources.map(source => getWordsSource(source, limit))
    words.foldLeft(List.empty[WordMerged]) {
      (merged,word_list) =>

    }

  }


}
