package com.overinfo.processors

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import scala.io.Source
import com.overinfo.PersistenceActor

/**
 * Created: Miguel A. Iglesias
 * Date: 1/14/14
 */

object TwitterSampler {

  trait Item {

    def twitter_id: Long

    def count: Int

    def item: String
  }

  case class Sample(items: Set[Item])

  trait Link extends Item

  case class Word(twitter_id: Long, item: String, count: Int) extends Item

  case class Tweet(origin: Long, text: String)

  trait HashTag extends Item

  trait TwitterUser extends Item

  val commonWords = Source.fromURL(getClass.getResource("/words.txt")).getLines().toSet

  val linkRegex = """((mailto\:|(news|(ht|f)tp(s?))\://){1}\S+)"""

  val linkPattern = linkRegex.r

}


class TwitterSampler(persistence: ActorRef) extends Actor {

  import TwitterSampler._

  def parseSample(origin: Long, text: String): Sample = {

    def tag(word: String, count: Int): Word = word match {
      case w if w.startsWith("#") => new Word(origin, word, count) with TwitterSampler.HashTag
      case w if w.startsWith("@") => new Word(origin, word, count) with TwitterSampler.TwitterUser
      case _ => Word(origin, word, count)
    }


    val links = linkPattern.findAllIn(text).toList.groupBy(l => l).map {
      case (l, ls) => new Word(origin, l, ls.size) with Link
    }.toSet
    val items = text
      .replaceAll(linkRegex, "")
      .split("[,\\.\" \"]")
      .filter(w => w.length > 0)
      .filterNot(w => commonWords.exists(_.toLowerCase.trim == w.toLowerCase.trim))
      .groupBy(w => w)
      .map {
      case (w, ws) => tag(w.toUpperCase, ws.size)
    }
    Sample(items.toSet ++ links)
  }

  def receive = LoggingReceive {
    case tweet@TwitterSampler.Tweet(origin, text) =>
      val sample = parseSample(origin, text)
      persistence ! PersistenceActor.Tweet(tweet,sample)
  }

}
