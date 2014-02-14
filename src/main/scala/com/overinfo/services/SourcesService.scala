package com.overinfo.services

import spray.routing.HttpService
import akka.actor.Actor
import spray.http.MediaTypes._
import com.overinfo.models.SourcesModel
import com.overinfo.mergers.WordsMerger.Sources

/**
 * Created: Miguel A. Iglesias
 * Date: 1/28/14
 */
class SourcesService extends Actor with SourcesRoutes {
  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(sourcesRoute)
}

trait SourcesRoutes extends HttpService {
  import SourcesModel._
  import spray.httpx.SprayJsonSupport._
  import spray.httpx.marshalling._
  import spray.json._



  val sourcesRoute = respondWithHeaders(corsHeaders: _*) {
    options {
      complete("ok")
    } ~
      path("sources") {
        get {
          complete(getSources)
        }
      }
  }


}
