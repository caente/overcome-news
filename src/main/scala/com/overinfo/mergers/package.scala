package com.overinfo

import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

/**
 * Created: Miguel A. Iglesias
 * Date: 1/27/14
 */
package object mergers {
  RegisterJodaTimeConversionHelpers()

//  trait Mergers {
//
//    def getWordsSource(source: Long, n: Int)(implicit exec: ExecutionContext): Future[WordMerger.Words] = Future {
//      WordMerger.Words(
//        source,
//        db("words").find(MongoDBObject("source" -> source)).limit(n).map {
//          dbo =>
//            WordMerger.Word(
//              dbo.get("_id").toString,
//              source,
//              dbo.getAs[String]("text").get,
//              dbo.getAs[String]("word").get,
//              dbo.getAs[Int]("count").get,
//              dbo.getAs[MongoDBList]("history").getOrElse(MongoDBList(DateTime.now)).map(d => new DateTime(d.asInstanceOf[Date])).toList.head
//            )
//        }.toList
//      )
//    }
//  }

}
