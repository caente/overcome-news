package com.overinfo.mergers

import akka.event.LoggingReceive
import akka.actor.{ActorRef, Actor}
import akka.pattern.pipe
import com.overinfo.models.WordsModel.TweetWord
import com.overinfo.models.SourcesModel
import org.joda.time.DateTime
import com.overinfo.mergers.WordMerger.{Diff, Intersect, MergeOperation}
import spray.json._
import DefaultJsonProtocol._

/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */

object WordMerger {

  case class Sources(sources: Set[Long], n: Int)

  case class Words(source: Long, words: List[TweetWord])

  case class MergedWord(source: SourcesModel.Source, word: String, count: Int, text: String, updated: DateTime) {
    override def equals(obj: scala.Any): Boolean = obj match {
      case w: MergedWord => w.word == this.word
      case t: TweetWord => t.word == this.word
      case _ => false
    }

    override def hashCode(): Int = this.word.toUpperCase.hashCode
  }

  case class MergedWords(words: List[MergedWord])

  object jsonProtocols {

    implicit object dateTimeFmt extends RootJsonFormat[DateTime] {
      def write(obj: DateTime): JsValue = JsString(obj.toString("MM-dd-yyyy"))

      def read(json: JsValue): DateTime = ???
    }

    implicit val sourcesFmt = jsonFormat2(Sources)
    implicit val sourceFmt = jsonFormat4(SourcesModel.Source)
    implicit val mergedWordFmt = jsonFormat5(MergedWord)
    implicit val mergedWordsFmt = jsonFormat1(MergedWords)
  }

  trait MergeOperation {

    def newest(w1: MergedWord, w2: TweetWord) =
      if (w1.updated.compareTo(w2.history.head) > 0)
        w1
      else
        MergedWord(w2.source, w2.word, w2.count, w2.text, w2.history.head)

    def selectDate(w1: MergedWord, w2: TweetWord): DateTime = newest(w1, w2).updated

    def selectSource(w1: MergedWord, w2: TweetWord): SourcesModel.Source = newest(w1, w2).source

    def selectText(w1: MergedWord, w2: TweetWord): String = newest(w1, w2).text

    def merge(
               m1: List[WordMerger.MergedWord],
               m2: List[TweetWord]
               ): List[WordMerger.MergedWord]
  }


  trait Intersect extends MergeOperation {
    def merge(
               m1: List[WordMerger.MergedWord],
               m2: List[TweetWord]): List[WordMerger.MergedWord] = {
      val int = m1.foldLeft(List.empty[MergedWord]) {
        (merged, w) =>
          m2.find(_.word == w.word) match {
            case (Some(m)) =>
              MergedWord(selectSource(w, m), m.word, m.count + w.count, selectText(w, m), selectDate(w, m)) :: merged
            case None => merged
          }
      }
      int
    }

  }

  trait Diff extends MergeOperation {
    def merge(m1: List[WordMerger.MergedWord], m2: List[TweetWord]): List[WordMerger.MergedWord] =
      m1.foldLeft(List.empty[MergedWord]) {
        (merged, w) =>
          m2.find(_.word == w.word) match {
            case (Some(m)) => merged
            case None => MergedWord(w.source, w.word, w.count, w.text, w.updated) :: merged
          }
      }
  }

}

abstract class WordMerger(parent: ActorRef) extends Actor with Mergers with MergeOperation {

  implicit val exec = context.dispatcher

  def receive = LoggingReceive {
    case WordMerger.Sources(sources, n) =>
      context become merging(sources)
      sources foreach {
        source =>
          getWordsSource(source, n) pipeTo self
      }
  }

  def merging(sources: Set[Long], mergedWords: List[WordMerger.MergedWord] = Nil): Receive = LoggingReceive {
    case WordMerger.Words(source, tweets) if mergedWords.isEmpty && sources.size == 1 =>
      parent ! WordMerger.MergedWords(buildMergeList(tweets))
      context stop self
    case WordMerger.Words(source, tweets) if mergedWords.isEmpty =>
      context become merging(sources - source, buildMergeList(tweets))
    case WordMerger.Words(source, tweets) if sources.size == 1 =>
      val merged = merge(mergedWords, tweets)
      parent ! WordMerger.MergedWords(merged)
      context stop self
    case WordMerger.Words(source, tweets) =>
      val merged = merge(mergedWords, tweets)
      context become merging(sources - source, merged)
  }

  def buildMergeList(tweets: List[TweetWord]) = {
    tweets map {
      case TweetWord(s, word, count, text, history) => WordMerger.MergedWord(s, word, count, text, history.head)
    }
  }

}

class WordIntersect(parent: ActorRef) extends WordMerger(parent) with Intersect

class WordDiff(parent: ActorRef) extends WordMerger(parent) with Diff
