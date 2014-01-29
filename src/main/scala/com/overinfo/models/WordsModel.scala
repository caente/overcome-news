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

  case class Updated(updated:DateTime)

  case class TweetWord(source: SourcesModel.Source, word: String, count: Int, text: String, history: List[Updated])

  RegisterJodaTimeConversionHelpers()

  def upsertWord(tweet: TwitterSampler.Tweet, item: TwitterSampler.Item) = {
    val update = $set("text" -> tweet.text) ++ $push("history" -> MongoDBObject("updated" -> DateTime.now)) ++ $inc("count" -> item.count)
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
          SourcesModel.getSource(source).get,
          dbo.getAs[String]("word").get,
          dbo.getAs[Int]("count").get,
          dbo.getAs[String]("word").get,
          dbo.getAs[List[Updated]]("history").get
        )
    }.toStream
  }


  def getWordsSources(sources: List[Long], n: Int): Future[Stream[TweetWord]] = {
    implicit def mergeStreams(t1: Stream[TweetWord]) = new {

      implicit def joinTweet(tweet: TweetWord) = new {

        def isNewer(otherTweet: TweetWord): Boolean = ??? //tweet.updated.compareTo(otherTweet.updated) >= 0

        def sum(otherTweet: TweetWord): TweetWord = {
          val newerTweet = if (isNewer(otherTweet)) tweet else otherTweet
          TweetWord(
            tweet.source,
            tweet.word,
            tweet.count + otherTweet.count,
            newerTweet.text,
            newerTweet.history
          )
        }
      }

      def mergeWith(t2: Stream[TweetWord]): Stream[TweetWord] = ???


    }
    def merge(tweets: List[Stream[TweetWord]], acc: Stream[TweetWord] = Stream.empty[TweetWord]): Stream[TweetWord] =
      tweets match {
        case Nil => acc
        case t :: ts => merge(ts, acc.mergeWith(t))
      }
    val wordsFuture = Future.traverse(sources)(source => getWordsSource(source, n))
    wordsFuture.map(streams => merge(streams))
  }


}
