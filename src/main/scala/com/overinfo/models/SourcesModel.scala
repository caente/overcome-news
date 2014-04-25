package com.overinfo.models

import twitter4j.User
import com.mongodb.casbah.Imports._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json._
import DefaultJsonProtocol._

/**
 * Created: Miguel A. Iglesias
 * Date: 1/24/14
 */
object SourcesModel extends Persistence with Limitable {

  case class Source(name: String, image_url: String, screen_name: String, twitter_id: Long)

  case class SourcesList(sources: List[Source])

  implicit val sourceProtocol = jsonFormat4(Source)
  implicit val sourcesListProtocol = jsonFormat1(SourcesList)

  def updateSources(sources: List[User]): Future[List[Long]] = Future {
    sources map {
      source =>
        val update = $set(
          "name" -> source.getName,
          "image_url" -> source.getBiggerProfileImageURL,
          "screen_name" -> source.getScreenName
        )
        db("sources").update(MongoDBObject("_id" -> source.getId), update, upsert = true)
        source.getId
    }
  }

  def getSources(limit: Option[Int], skip:Option[Int]): SourcesList = SourcesList(db("sources").find().map {
    dbo =>
      Source(
        dbo.getAs[String]("name").getOrElse(""),
        dbo.getAs[String]("image_url").getOrElse(""),
        dbo.getAs[String]("screen_name").getOrElse(""),
        dbo.getAs[Long]("_id").getOrElse(0L)
      )
  }.toList.skipOption(skip).limitOption(limit))

  def getSource(_id: Long): Option[Source] = db("sources").findOne(MongoDBObject("_id" -> _id)).map {
    dbo =>
      Source(
        dbo.getAs[String]("name").getOrElse(""),
        dbo.getAs[String]("image_url").getOrElse(""),
        dbo.getAs[String]("screen_name").getOrElse(""),
        _id
      )
  }

}
