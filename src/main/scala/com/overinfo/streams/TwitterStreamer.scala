package com.overinfo.streams

import twitter4j._
import com.overinfo.processors.TwitterSampler
import akka.actor.{ActorRef, Terminated, Actor, PoisonPill}
import com.overinfo.streams.TwitterStreamer.TwitterCredentials
import akka.event.LoggingReceive
import com.overinfo.models.TwitterAccountModel.TwitterAccount
import twitter4j.conf.Configuration


/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */

object TwitterStreamer {

  case class TwitterCredentials(
                                 consumer_key: String,
                                 consumer_secret: String,
                                 access_token: String,
                                 token_secret: String
                                 )

  case class Start(twitterAccount: TwitterAccount)

  case object Stop

  case object NotStarted

  case object Initialized

}

class TwitterStreamer extends Actor with TwitterStreamerActors {

  var twitterStream: TwitterStream = _


  def receive = LoggingReceive {
    case TwitterStreamer.Start(TwitterAccount(account_id, TwitterCredentials(consumer_key, consumer_secret, access_token, token_secret))) =>
      val sampler = twitterSampler(account_id)
      val configTwitter = new twitter4j.conf.ConfigurationBuilder()
        .setOAuthConsumerKey(consumer_key)
        .setOAuthConsumerSecret(consumer_secret)
        .setOAuthAccessToken(access_token)
        .setOAuthAccessTokenSecret(token_secret)
        .build
      twitterStream = new TwitterStreamFactory(configTwitter).getInstance
      twitterStream.addListener(userStreamListener(sampler))
      twitterStream.user()
    case TwitterStreamer.Stop =>
      twitterStream.shutdown()
      twitterStream.cleanUp()
      context.stop(self)

  }


  def userStreamListener(sampler: ActorRef) = new UserStreamListener {
    def onFriendList(friendIds: Array[Long]): Unit = {}

    def onUserListUnsubscription(subscriber: User, listOwner: User, list: UserList): Unit = {}

    def onStallWarning(warning: StallWarning): Unit = {}

    def onBlock(source: User, blockedUser: User): Unit = {}

    def onUserListSubscription(subscriber: User, listOwner: User, list: UserList): Unit = {}

    def onFollow(source: User, followedUser: User): Unit = {}

    def onUserListMemberAddition(addedMember: User, listOwner: User, list: UserList): Unit = {}

    def onDirectMessage(directMessage: DirectMessage): Unit = {}

    def onUserListUpdate(listOwner: User, list: UserList): Unit = {}

    def onUnblock(source: User, unblockedUser: User): Unit = {}

    def onUserProfileUpdate(updatedUser: User): Unit = {}

    def onException(ex: Exception): Unit = {}

    def onUserListMemberDeletion(deletedMember: User, listOwner: User, list: UserList): Unit = {}

    def onDeletionNotice(directMessageId: Long, userId: Long): Unit = {}

    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

    def onFavorite(source: User, target: User, favoritedStatus: Status): Unit = {}

    def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}

    def onUnfavorite(source: User, target: User, unfavoritedStatus: Status): Unit = {}

    def onStatus(status: Status): Unit = {
      sampler ! TwitterSampler.Tweet(status.getUser.getId, status.getText)
      println(s"@${status.getUser.getScreenName} - ${status.getText}")
    }

    def onUserListDeletion(listOwner: User, list: UserList): Unit = {}

    def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}

    def onUserListCreation(listOwner: User, list: UserList): Unit = {}
  }


}
