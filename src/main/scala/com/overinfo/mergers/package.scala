package com.overinfo

import scala.concurrent.Future
import com.overinfo.models._
import com.mongodb.casbah.Imports._
import com.overinfo.models.WordsModel.{Updated, TweetWord}

/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */
package object mergers {

  trait Mergers {
    def getWordsSource(source: Long, n: Int): Future[WordMerger.Words] = Future {
      WordMerger.Words(source, db("words").find(MongoDBObject("source" -> source)).limit(n).map {
        dbo =>
          TweetWord(
            SourcesModel.getSource(source).get,
            dbo.getAs[String]("word").get,
            dbo.getAs[Int]("count").get,
            dbo.getAs[String]("word").get,
            dbo.getAs[List[Updated]]("history").get
          )
      }.toList)
    }
  }

}
