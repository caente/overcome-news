package com.overinfo.mergers

import akka.event.LoggingReceive
import akka.actor.{ActorRef, Actor}
import akka.pattern.pipe
import com.overinfo.models.WordsModel.TweetWord
import com.overinfo.models.SourcesModel
import org.joda.time.DateTime
import com.overinfo.mergers.WordMerger.{Diff, Intersect, MergeOperation}


/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */

object WordMerger {

  case class Sources(sources: List[Long], n: Int)

  case class Source(source: Long, n: Int)

  case class Words(source: Long, words: List[TweetWord])

  case class MergedWord(source: SourcesModel.Source, word: String, count: Int, text: String, updated: DateTime)

  case class MergedWords(words: List[MergedWord])

  trait MergeOperation {

    def newest(w1: MergedWord, w2: TweetWord) =
      if (w1.updated.compareTo(w2.history.head.updated) > 0)
        w1
      else
        MergedWord(w2.source, w2.word, w2.count, w2.text, w2.history.head.updated)

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
               m2: List[TweetWord]): List[WordMerger.MergedWord] =
      m1.foldLeft(List.empty[MergedWord]) {
        (merged, w) =>
          m2.find(_.word == w.word) match {
            case (Some(m)) =>
              MergedWord(selectSource(w, m), m.word, m.count + w.count, selectText(w, m), selectDate(w, m)) :: merged
            case None => merged
          }
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

  def receive = LoggingReceive {
    case WordMerger.Sources(sources, n) =>
      context become merging(sources)
      sources foreach {
        source =>
          getWordsSource(source, n) pipeTo self
      }
  }

  def merging(sources: List[Long], mergedWords: List[WordMerger.MergedWord] = Nil): Receive = LoggingReceive {
    case WordMerger.Words(source, tweets) if mergedWords.isEmpty =>
      context become merging(sources.dropWhile(_ == source), buildMergeList(tweets))
    case WordMerger.Words(source, tweets) if sources.length == 1 =>
      parent ! WordMerger.MergedWords(merge(mergedWords, tweets))
      context stop self
    case WordMerger.Words(source, tweets) =>
      context become merging(sources.dropWhile(_ == source), merge(mergedWords, tweets))
  }

  def buildMergeList(tweets: List[TweetWord]) = tweets map {
    case TweetWord(s, word, count, text, history) => WordMerger.MergedWord(s, word, count, text, history.head.updated)
  }

}

class WordIntersect(parent: ActorRef) extends WordMerger(parent) with Intersect

class WordDiff(parent: ActorRef) extends WordMerger(parent) with Diff
