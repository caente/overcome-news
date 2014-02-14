package com.overinfo.mergers

import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import akka.actor.{Props, ActorSystem}
import org.specs2.mutable.SpecificationLike
import org.specs2.time.NoTimeConversions
import com.overinfo.models.db
import com.mongodb.casbah.Imports._
import com.overinfo.mergers.WordsMergerSpec.AggregationResult
import org.specs2.specification.Scope
import com.overinfo.mergers.WordsMerger.{MergingResult, Sources}

/**
 * Created: Miguel A. Iglesias
 * Date: 2/3/14
 */

object WordsMergerSpec {

  case class AggregationResult(word: String, count: Int, sources: Set[Long])

}

class WordsMergerSpec extends TestKit(ActorSystem("testing-merger")) with ImplicitSender
with SpecificationLike with NoTimeConversions {
  "WordsMerger" should {
    "receive merges" in new BasicScope {
      override lazy val limit = 10
      aggregatedResults.foreach {
        case AggregationResult(word, count, sources) =>
          val parent = TestProbe()
          val intersectMerger = system.actorOf(Props(new WordIntersect(parent.ref)))
          intersectMerger ! Sources(sources, limit)
          println(s"testing for $word")
          parent.receiveWhile() {
            case m@MergingResult(c, words) if c == 0 && words.words.isEmpty => ()
            case m@MergingResult(c, words) if c == 0 =>
              println(m)
              words.words.find(_.word == word).get.count should be equalTo count
            case m => println(m)
          }
      }
    }

    trait BasicScope extends Scope {

      lazy val limit = 500

      //db.words.aggregate({$group: {_id:"$word",count:{$sum:"$count"},sources: {$push: "$source"}}},{$sort: {count:1}})
      private val aggregated = db("words").aggregate(
//        MongoDBObject("$sort" -> MongoDBObject("history" -> -1)),
        MongoDBObject(
          "$group" -> MongoDBObject(
            "_id" -> "$word",
            "count" -> MongoDBObject("$sum" -> "$count"),
            "sources" -> MongoDBObject("$push" -> "$source")
          )
        ),
        MongoDBObject("$sort" -> MongoDBObject("count" -> -1)),
        MongoDBObject("$limit" -> limit)
      )
      val aggregatedResults = aggregated.results.map {
        dbo =>
          AggregationResult(
            dbo.get("_id").toString,
            dbo.getAs[Int]("count").get,
            dbo.getAs[MongoDBList]("sources").get.map(_.asInstanceOf[Long]).toSet
          )
      } take 1
    }
  }
}
