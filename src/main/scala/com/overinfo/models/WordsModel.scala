package com.overinfo.models

import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.overinfo.processors.TwitterSampler
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created: Miguel A. Iglesias
 * Date: 1/25/14
 */
object WordsModel {

  case class TweetWord(_id:String, source: SourcesModel.Source, word: String, count: Int, text: String, history: List[DateTime])


  RegisterJodaTimeConversionHelpers()

  def upsertWord(tweet: TwitterSampler.Tweet, item: TwitterSampler.Item) = {
    val update = $set("text" -> tweet.text) ++ $push("history" -> DateTime.now) ++ $inc("count" -> item.count)
    db("words").update(
      MongoDBObject("source" -> tweet.origin, "word" -> item.item),
      update,
      upsert = true
    )
  }

  def getWordsSource(source: Long, n: Int): Future[Stream[TweetWord]] = Future {
    db("words").find(MongoDBObject("source" -> source)).limit(n).map {
      dbo =>
        TweetWord(
         dbo.get("_id").toString,
          SourcesModel.getSource(source).get,
          dbo.getAs[String]("word").get,
          dbo.getAs[Int]("count").get,
          dbo.getAs[String]("word").get,
          dbo.getAs[List[DateTime]]("history").get
        )
    }.toStream
  }


}
