package com.overinfo

import akka.actor.Actor
import com.overinfo.processors.TwitterSampler
import akka.event.LoggingReceive
import scala.concurrent.Future
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

/**
 * Created: Miguel A. Iglesias
 * Date: 1/15/14
 */


object PersistenceActor {

  case class Tweet(tweet: TwitterSampler.Tweet, sample: TwitterSampler.Sample)

}

class PersistenceActor extends Actor {

  import models._

  implicit val exec = context.dispatcher

  def receive = LoggingReceive {
    case PersistenceActor.Tweet(tweet, sample) => sample.items foreach {
      item =>
        Future(WordsModel.upsertWord(tweet, item))
    }
  }
}
