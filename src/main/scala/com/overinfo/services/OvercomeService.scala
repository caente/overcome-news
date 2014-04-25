package com.overinfo.services

import spray.routing.HttpService
import akka.actor.Actor


/**
 * Created: Miguel A. Iglesias
 * Date: 1/28/14
 */
class OvercomeService extends Actor with OvercomeRoutes {

  def actorRefFactory = context

  def receive = runRoute(sourcesRoute)
}

trait OvercomeRoutes extends HttpService with Sources with Words {

  import Headers._

  val options_request = options {
    complete("ok")
  }

  val sourcesRoute =
    respondWithHeaders(corsHeaders: _*) {
      options_request ~
        sources_get ~
        words_intersect
    }


}
