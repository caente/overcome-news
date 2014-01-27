package com.overinfo.streams

import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import akka.actor.{Actor, Props, ActorSystem}
import org.specs2.mutable.SpecificationLike
import org.specs2.time.NoTimeConversions
import com.overinfo.PersistenceActor
import com.overinfo.processors.TwitterSampler
import com.overinfo.models.TwitterAccountModel
import akka.event.LoggingReceive

/**
 * Created: Miguel A. Iglesias
 * Date: 1/23/14
 */
class TwitterStreamerSpec extends TestKit(ActorSystem("testing")) with ImplicitSender
with SpecificationLike with NoTimeConversions {
  "TwitterStreamer" should {
    "stream to earth" in {
      val twitterAccount = TwitterAccountModel.getTwitterAccount(1407921984).get
      val twitterStreamer = system.actorOf(
        Props(new TwitterStreamer with TwitterStreamerActorsMock), s"account-${twitterAccount.account_id}-stream")
      twitterStreamer ! TwitterStreamer.Start(twitterAccount)
      Thread.sleep(600000)
      twitterStreamer ! TwitterStreamer.Stop
      true should be equalTo true
    }
  }

  trait TwitterStreamerActorsMock extends TwitterStreamerActors {
    val persistenceProbe = TestProbe()
    override def persistence(account_id:Long) = system.actorOf(Props(new Actor {
      def receive = LoggingReceive {
        case m =>
          persistenceProbe.ref ! m
          println(m)
      }
    }),name = s"account-$account_id-persistence")
    val persistence2 = system.actorOf(Props(new PersistenceActor))
    override def twitterSampler(account_id:Long) = system.actorOf(Props(new TwitterSampler(persistence2))
      ,name = s"account-$account_id-sampler")
  }

}
