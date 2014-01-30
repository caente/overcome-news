package com.overinfo.services

import spray.routing.HttpService
import akka.actor.{Props, Actor}
import com.overinfo.mergers.{WordDiff, WordIntersect}

import spray.routing.RequestContext
import com.overinfo.mergers.WordMerger.Sources
import com.overinfo.mergers.WordMerger.MergedWords
import akka.event.LoggingReceive


/**
 * Created: Miguel A. Iglesias
 * Date: 1/28/14
 */
class WordsService extends Actor with WordRoutes {

  def actorRefFactory = context

  def receive = runRoute(wordOperations)

}


trait WordRoutes extends HttpService {

  import com.overinfo.mergers.WordMerger.jsonProtocols._
  import spray.httpx.SprayJsonSupport._
  import spray.httpx.marshalling._
  import spray.httpx.unmarshalling._
  import spray.json._

  val wordOperations =
    path("words" / "intersect") {
      post {
        entity(as[Sources]) {
          sources =>
            intersect(sources)
        }
      }
    } ~
      path("words" / "diff") {
        post {
          entity(as[Sources]) {
            sources =>
              diff(sources)
          }
        }
      }


  def intersect(sources: Sources)(ctx: RequestContext): Unit = {
    val sender = actorRefFactory.actorOf(Props(new Actor {
      def receive = LoggingReceive {
        case words: MergedWords =>
          ctx.complete(words)
      } andThen(_ => context.stop(self))
    }), name = s"waiter")
    actorRefFactory.actorOf(Props(new WordIntersect(sender)),name  = s"intersect") ! sources
  }

  def diff(sources: Sources)(ctx: RequestContext): Unit = {
    val sender = actorRefFactory.actorOf(Props(new Actor {
      def receive = LoggingReceive {
        case words: MergedWords =>
          ctx.complete(words)
      } andThen(_ => context.stop(self))
    }), name = s"waiter")
    actorRefFactory.actorOf(Props(new WordDiff(sender)),name  = s"intersect") ! sources
  }
}
