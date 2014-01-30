package com.overinfo

import scala.concurrent.{ExecutionContext, Future}
import com.overinfo.models._
import com.mongodb.casbah.Imports._
import com.overinfo.models.WordsModel.{Updated, TweetWord}
import org.joda.time.DateTime
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import java.util.Date

/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */
package object mergers {
  RegisterJodaTimeConversionHelpers()
  trait Mergers {
    def getWordsSource(source: Long, n: Int)(implicit exec:ExecutionContext): Future[WordMerger.Words] = Future {
      WordMerger.Words(source, db("words").find(MongoDBObject("source" -> source)).limit(n).map {
        dbo =>
          TweetWord(
            SourcesModel.getSource(source).get,
            dbo.getAs[String]("word").get,
            dbo.getAs[Int]("count").get,
            dbo.getAs[String]("text").get,
            dbo.getAs[MongoDBList]("history").getOrElse(MongoDBList()).map(_.asInstanceOf[DBObject]).map {
              d =>
                Updated(new DateTime(d.getAs[Date]("updated").getOrElse(new Date)))
            }.toList
          )
      }.toList)
    }
  }

}
