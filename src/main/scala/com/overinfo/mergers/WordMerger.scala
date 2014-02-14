package com.overinfo.mergers



/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */

//object WordMerger {
//
//
//  trait WordLike {
//    def word: String
//
//    def count: String
//
//    def text: String
//  }
//
//  case class Sources(sources: Set[Long], n: Int)
//
//  case class Words(source: Long, words: List[Word])
//
//  case class Word(id: String, source: Long, text: String, word: String, count: Int, last_update: DateTime) extends WordLike {
//    override def equals(obj: scala.Any): Boolean = obj match {
//      case w: MergedWord => w.word == this.word
//      case t: Word => t.word == this.word
//      case _ => false
//    }
//  }
//
//    case class MergedWord(word: String, count: Int, tweets: List[Word], updated: DateTime) extends WordLike {
//      override def equals(obj: scala.Any): Boolean = obj match {
//        case w: MergedWord => w.word == this.word
//        case t: Word => t.word == this.word
//        case _ => false
//      }
//
//      override def hashCode(): Int = this.word.toUpperCase.hashCode
//    }
//
//    case class MergedWords(words: List[MergedWord])
//
//    object jsonProtocols {
//
//      implicit object dateTimeFmt extends RootJsonFormat[DateTime] {
//        def write(obj: DateTime): JsValue = JsString(obj.toString("MM-dd-yyyy"))
//
//        def read(json: JsValue): DateTime = ???
//      }
//
//      implicit val sourcesFmt = jsonFormat2(Sources)
//      implicit val sourceFmt = jsonFormat4(SourcesModel.Source)
//      implicit val tweetFmt = jsonFormat6(Word)
//      implicit val mergedWordFmt = jsonFormat4(MergedWord)
//      implicit val mergedWordsFmt = jsonFormat1(MergedWords)
//    }
//
//    trait MergeOperation {
//
//      def newest(w1: MergedWord, w2: Word) =
//        if (w1.updated.compareTo(w2.last_update) > 0)
//          w1
//        else
//          MergedWord(w2.word, w2.count, List(Word(w2.id, w2.source, w2.text, w2.word, w2.count, w2.last_update)), w2.last_update)
//
//      def selectDate(w1: MergedWord, w2: Word): DateTime = newest(w1, w2).updated
//
//      def merge(
//                 m1: List[WordMerger.MergedWord],
//                 m2: List[Word]
//                 ): List[WordMerger.MergedWord]
//    }
//
//
//    trait Intersect extends MergeOperation {
//      def merge(
//                 m1: List[WordMerger.MergedWord],
//                 m2: List[Word]): List[WordMerger.MergedWord] = {
//        val int = m1.foldLeft(List.empty[MergedWord]) {
//          (merged, w) =>
//            m2.find(_.word == w.word) match {
//              case (Some(m)) =>
//                MergedWord(m.word, m.count + w.count, Word(m.id, m.source, m.text, m.word, m.count, m.last_update) :: w.tweets, selectDate(w, m)) :: merged
//              case None => merged
//            }
//        }
//        int
//      }
//
//    }
//
//    trait Diff extends MergeOperation {
//      def merge(m1: List[WordMerger.MergedWord], m2: List[Word]): List[WordMerger.MergedWord] =
//        m1.foldLeft(List.empty[MergedWord]) {
//          (merged, w) =>
//            m2.find(_.word == w.word) match {
//              case (Some(m)) => merged
//              case None => MergedWord(w.word, w.count, List(), w.updated) :: merged
//            }
//        }
//    }
//
//  }
//
//  abstract class WordMerger(parent: ActorRef) extends Actor with Mergers with MergeOperation {
//
//    implicit val exec = context.dispatcher
//
//    def receive = LoggingReceive {
//      case WordMerger.Sources(sources, n) =>
//        context become merging(sources)
//        sources foreach {
//          source =>
//            getWordsSource(source, n) pipeTo self
//        }
//    }
//
//    def merging(sources: Set[Long], mergedWords: List[WordMerger.MergedWord] = Nil): Receive = LoggingReceive {
//      case WordMerger.Words(source, tweets) if mergedWords.isEmpty && sources.size == 1 =>
//        parent ! WordMerger.MergedWords(buildMergeList(tweets))
//        context stop self
//      case WordMerger.Words(source, tweets) if mergedWords.isEmpty =>
//        context become merging(sources - source, buildMergeList(tweets))
//      case WordMerger.Words(source, tweets) if sources.size == 1 =>
//        val merged = merge(mergedWords, tweets)
//        parent ! WordMerger.MergedWords(merged)
//        context stop self
//      case WordMerger.Words(source, tweets) =>
//        val merged = merge(mergedWords, tweets)
//        context become merging(sources - source, merged)
//    }
//
//    def buildMergeList(tweets: List[WordMerger.Word]): List[MergedWord] = {
//      tweets map {
//        case tweet@Word(_id, s, text, word, count, last_updated) => WordMerger.MergedWord(word, count, List(Word(_id, s, text, word, count, last_updated)), last_updated)
//      }
//    }
//
//  }
//
//  class WordIntersect(parent: ActorRef) extends WordMerger(parent) with Intersect
//
//  class WordDiff(parent: ActorRef) extends WordMerger(parent) with Diff
