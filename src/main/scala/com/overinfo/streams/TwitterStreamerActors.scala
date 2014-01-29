package com.overinfo.streams

import akka.actor.Props
import com.overinfo.processors.{Persistence, TwitterSampler}

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */
trait TwitterStreamerActors extends Persistence {
 def twitterSampler(account_id:Long) = system.actorOf(
   Props(new TwitterSampler(persistence(account_id))),name = s"account-$account_id-sampler")
}
