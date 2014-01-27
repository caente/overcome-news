package com.overinfo.models

import com.overinfo.streams.TwitterStreamer.TwitterCredentials
import scala.util.{Failure, Success, Try}
import twitter4j.{User, Twitter, TwitterFactory}
import twitter4j.auth.AccessToken
import scala.annotation.tailrec
import scala.collection.JavaConverters._
import com.mongodb.casbah.Imports._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */


object TwitterAccountModel {

  case class TwitterAccount(account_id: Long, twitterCredentials: TwitterCredentials)

  @tailrec
  def twitterFollowees(twitterClient: Twitter, followeesList: List[User] = Nil,
                       cursorFollowees: Long = -1L): List[User] = {
    val followees = twitterClient.getFriendsList(twitterClient.getScreenName, cursorFollowees)
    if (followees.isEmpty)
      followeesList
    else {
      twitterFollowees(
        twitterClient,
        followees.asScala.toList ++ followeesList,
        cursorFollowees = followees.getNextCursor
      )
    }
  }

  def addTwitterAccount(twitterCredentials: TwitterCredentials): Future[Try[Int]] = Future {
    val twitterClient = (new TwitterFactory).getInstance
    twitterClient.setOAuthConsumer(twitterCredentials.consumer_key, twitterCredentials.consumer_secret)
    twitterClient.setOAuthAccessToken(new AccessToken(twitterCredentials.access_token, twitterCredentials.token_secret))
    db("twitter_accounts").update(
      MongoDBObject("_id" -> twitterClient.getId),
      $set(
        "consumer_key" -> twitterCredentials.consumer_key,
        "consumer_secret" -> twitterCredentials.consumer_secret,
        "access_token" -> twitterCredentials.access_token,
        "token_secret" -> twitterCredentials.token_secret
      ),
      upsert = true
    ).getN match {
      case n if n > 0 =>
        //TODO if the update of the sources goes wrong, we need to check if none was added and propagate the error
        SourcesModel.updateSources(twitterFollowees(twitterClient))
        Success(n)
      case _ => Failure(new Error("Didn't save"))
    }
  }

  def getTwitterAccounts: Stream[TwitterAccount] = {
    db("twitter_accounts").find().map {
      dbo =>
        TwitterAccount(
          dbo.getAs[Long]("_id").get,
          TwitterCredentials(
            dbo.get("consumer_key").toString,
            dbo.get("consumer_secret").toString,
            dbo.get("access_token").toString,
            dbo.get("token_secret").toString
          )
        )
    }.toStream
  }

  def getTwitterAccount(_id: Long): Option[TwitterAccount] =
    db("twitter_accounts").findOne(MongoDBObject("_id" -> _id)).map {
      dbo =>
        TwitterAccount(
          _id,
          TwitterCredentials(
            dbo.get("consumer_key").toString,
            dbo.get("consumer_secret").toString,
            dbo.get("access_token").toString,
            dbo.get("token_secret").toString
          )
        )
    }

}
