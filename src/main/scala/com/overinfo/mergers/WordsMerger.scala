package com.overinfo.mergers

import akka.actor.{Actor, ActorRef}
import org.joda.time.DateTime
import scala.concurrent._
import com.overinfo.models._
import com.mongodb.casbah.Imports._
import java.util.Date
import akka.pattern.pipe
import akka.event.LoggingReceive
import spray.json._
import DefaultJsonProtocol._
import scala.Some

/**
 * Created: Miguel A. Iglesias
 * Date: 2/1/14
 */

object WordsMerger {

  case class Word(id: String, source: Long, text: String, word: String, count: Int, last_update: DateTime)

  case class Words(source: Long, words: Set[Word]) {
    override def toString = s"Words($source,${words.size})"
  }

  case class WordMergedDetail(word_id: String, source: Long, last_updated: DateTime, count: Int)

  case class WordMerged(word: String, count: Int, details: List[WordMergedDetail])

  case class WordsMerged(words: Set[WordMerged]) {
    override def toString = s"WordsMerged(${words.size})"
  }

  case class Sources(sources: Set[Long], n: Int)

  case class MergingResult(sourcesLeft: Int, wordsMerged: WordsMerged)

  implicit object dateTimeProtocol extends RootJsonFormat[DateTime] {
    def write(obj: DateTime): JsValue = JsString(obj.toString("MM-dd-yyyy"))

    def read(json: JsValue): DateTime = ???
  }

  implicit val sourcesProtocol = jsonFormat2(Sources)
  implicit val wordMergedDetail = jsonFormat4(WordMergedDetail)
  implicit val wordMergedProtocol = jsonFormat3(WordMerged)
  implicit val wordsMergedProtocol = jsonFormat1(WordsMerged)
  implicit val mergingResultsProtocol = jsonFormat2(MergingResult)

  case class WorkingOn(sources: Int)

  import scala.concurrent.duration._

  def delay(t: Duration)(block: => Unit) = Future {
    blocking {
      Thread.sleep(t.toMillis)
    }
  } onComplete(_ => block)


  val msg = "message"

  def send_message(msg:String) = println(msg)

  while (true) delay(1 second)(send_message(msg))
}


import com.overinfo.mergers.WordsMerger._

trait Merge {
  def merge(words: Set[Word], wordsMerged: Set[WordMerged]): Set[WordMerged]

  def getWordsSource(source: Long, n: Int)(implicit exec: ExecutionContext): Future[Words] = Future {
    Words(
      source,
      db("words").find(MongoDBObject("source" -> source)).sort(MongoDBObject("history" -> -1)).limit(n).map {
        dbo =>
          Word(
            dbo.get("_id").toString,
            source,
            dbo.getAs[String]("text").get,
            dbo.getAs[String]("word").get,
            dbo.getAs[Int]("count").get,
            dbo.getAs[MongoDBList]("history").getOrElse(MongoDBList(new Date())).map(d => new DateTime(d.asInstanceOf[Date])).toList.head
          )
      }.toSet
    )
  }
}

abstract class WordsMerger(parent: ActorRef) extends Actor with Merge {

  implicit val exec = context.dispatcher

  def receive = LoggingReceive {
    case Sources(sources, n) =>
      context become merging(sources)
      sources.map {
        source =>
          getWordsSource(source, n) pipeTo self
      }
    //      val merged = sourceWords.foldLeft(Set.empty[WordMerged]){
    //        (m,w) =>
    //          merge(w.words,m)
    //      }
    //      parent ! MergingResult(0,WordsMerged(merged))
    //      context.stop(self)

  }

  def merging(sources: Set[Long], wordsMerged: Set[WordMerged] = Set.empty[WordMerged]): Receive = LoggingReceive {

    case Words(source, words) if sources.size == 1 =>
      val merges = merge(words, wordsMerged)
      parent ! MergingResult(sources.size - 1, WordsMerged(merges))
      context.stop(self)
    case Words(source, words) =>
      val merges = merge(words, wordsMerged)
      context become merging(sources - source, merges)
  }

}

trait Intersect extends Merge {
  def merge(words: Set[Word], wordsMerged: Set[WordMerged]): Set[WordMerged] = {
    if (wordsMerged.isEmpty)
      words.map {
        case Word(id, source, text, word, count, last_updated) =>
          WordMerged(word, count, WordMergedDetail(id, source, last_updated, count) :: Nil)
      }
    else {
      val wordsList = words.map(_.word)
      val mergedList = wordsMerged.map(_.word)
      val intersected = mergedList.intersect(wordsList)
      intersected.foldLeft(Set.empty[WordMerged]) {
        (merged, wordIntersected) =>
          (words.find(_.word == wordIntersected), wordsMerged.find(_.word == wordIntersected)) match {
            case (Some(Word(id, source, text, word, count, last_updated)), Some(WordMerged(wordMerged, countMerged, details))) =>
              merged + WordMerged(wordIntersected, count + countMerged, WordMergedDetail(id, source, last_updated, count) :: details)
          }
      }
    }

  }

  //    words.foldLeft(Set.empty[WordMerged]) {
  //      case (merges, w) if wordsMerged.isEmpty =>
  //        merges + WordMerged(w.word, w.count, WordMergedDetail(w.id, w.source, w.last_update, w.count) :: Nil)
  //      case (merges, w) =>
  //        wordsMerged.find(_.word == w.word) match {
  //          case Some(WordMerged(word, count, details)) =>
  //            merges + WordMerged(word, count + w.count, WordMergedDetail(w.id, w.source, w.last_update, w.count) :: details)
  //          case None => merges
  //        }
  //    }
}

class WordIntersect(parent: ActorRef) extends WordsMerger(parent) with Intersect
