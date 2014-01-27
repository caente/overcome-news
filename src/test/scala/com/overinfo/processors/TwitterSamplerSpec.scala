package com.overinfo.processors

import akka.actor.Actor
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.specs2.mutable.SpecificationLike
import org.specs2.time.NoTimeConversions
import scala.io.Source
import com.overinfo.PersistenceActor

/**
 * Created: Miguel A. Iglesias
 * Date: 1/15/14
 */
class TwitterSamplerSpec extends TestKit(ActorSystem("testing")) with ImplicitSender
with SpecificationLike with NoTimeConversions {
  import TwitterSampler._
  "TwitterSampler" should {
    "find stuff" in {
      val persistenceProbe = TestProbe()
      val persistence = system.actorOf(Props(new Actor {
        def receive = {
          case m =>
            persistenceProbe.ref ! m
        }
      }))
      val persistence2 = system.actorOf(Props(new PersistenceActor))
      val twitterSampler = system.actorOf(Props(new TwitterSampler(persistence)))
      val tweets = Source.fromURL(getClass.getResource("/samples.txt")).getLines().toSet
      tweets.foreach(t => twitterSampler ! TwitterSampler.Tweet(1, t))
      val expected = PersistenceActor.Tweet(Tweet(1,"Suntory billionaire taps Jim Beam as family business goes global | http://bloom.bg/1altLOG"),Sample(Set(Word(1,"GLOBAL",1), Word(1,"http://bloom.bg/1altLOG",1), Word(1,"BEAM",1), Word(1,"SUNTORY",1), Word(1,"TAPS",1), Word(1,"FAMILY",1), Word(1,"JIM",1), Word(1,"BILLIONAIRE",1), Word(1,"GOES",1), Word(1,"BUSINESS",1))))
      persistenceProbe.expectMsg(expected)
      true should be equalTo true
    }
  }

}
