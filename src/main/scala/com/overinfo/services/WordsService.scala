package com.overinfo.services

import spray.routing.HttpService
import akka.actor.{Props, Actor}
import com.overinfo.mergers.WordIntersect
import spray.http._
import spray.http.MediaTypes._
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.{MergingResult, Sources}
import scala.concurrent.duration._
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import akka.io.Tcp
import scala.collection.immutable.Queue
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.http.HttpResponse
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.http.ChunkedResponseStart
import spray.util._
import spray.http.HttpResponse
import spray.routing.RequestContext
import com.overinfo.mergers.WordsMerger.Sources
import com.overinfo.mergers.WordsMerger.MergingResult
import spray.http.ChunkedResponseStart

//import com.overinfo.mergers.{WordDiff, WordIntersect}

import spray.routing.RequestContext

//import com.overinfo.mergers.WordMerger.Sources
//import com.overinfo.mergers.WordMerger.MergedWords

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

  import com.overinfo.mergers.WordsMerger._

  import spray.httpx.SprayJsonSupport._
  import spray.httpx.marshalling._
  import spray.httpx.unmarshalling._
  import spray.json._


    val wordOperations = respondWithHeaders(corsHeaders: _*) {
        options {
          complete("ok")
        } ~
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
            //          entity(as[Sources]) {
            //            sources =>
            complete("ok")
            //              diff(sources)
            //          }
          }
        }
    }

  case class Ok(remaining: Int)

  def intersect(sources: Sources)(ctx: RequestContext): Unit = {
    actorRefFactory.actorOf(Props(new Actor {




      implicit val exec = context.dispatcher

      val intersect =  actorRefFactory.actorOf(Props(new WordIntersect(self)), name = s"intersect")

      intersect ! sources

      def receive = LoggingReceive {
        case MergingResult(s, wordsMerged) =>
           ctx.complete(wordsMerged)
           context.stop(intersect)
      } andThen(_ => context.stop(self))

//         val responseStart = HttpResponse(entity = HttpEntity(`text/plain`, "STARTING"))
//      ctx.responder ! ChunkedResponseStart(responseStart).withAck(Ok(sources.sources.size))
//
//      def in[U](duration: FiniteDuration)(body: => U): Unit =
//        actorSystem.scheduler.scheduleOnce(duration)(body)
//
//      var queue = Queue.empty[HttpResponsePart]

//      def waitingAck: Receive = LoggingReceive {
//        case WorkingOn(remaining) =>
//          queue = queue.enqueue(MessageChunk(Ok(remaining)))
//        case Ok(0) =>
//          ctx.responder ! ChunkedMessageEnd
//          context.stop(self)
//        case Ok(remaining) if queue.isEmpty =>
//          context become waitingMerging(remaining)
//         intersect ! sources
//        case Ok(remaining) =>
//          val (chunk, q) = queue.dequeue
//          queue = q
//          ctx.responder ! chunk.withAck(Ok(remaining - 1))
//      }
//
//      def waitingMerging(ack: Int) = LoggingReceive {
//        case m@MergingResult(s, wordsMerged) =>
//          context become waitingAck
//          ctx.responder ! MessageChunk(wordsMerged.toJson.toString()).withAck(Ok(ack - 1))
//        case ev: Tcp.ConnectionClosed =>
//          println(s"Stopping response streaming due to $ev")
//      }
    }), name = s"waiter")

  }

  //
  //  def diff(sources: Sources)(ctx: RequestContext): Unit = {
  //    val sender = actorRefFactory.actorOf(Props(new Actor {
  //      def receive = LoggingReceive {
  //        case words: MergedWords =>
  //          ctx.complete(words)
  //      } andThen (_ => context.stop(self))
  //    }), name = s"waiter")
  //    actorRefFactory.actorOf(Props(new WordDiff(sender)), name = s"diff") ! sources
  //  }
}
