package com.overinfo.services

import spray.routing.HttpService
import akka.actor.Actor
import spray.http.MediaTypes._

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
  def receive = runRoute(myRoute)
}

trait SourcesRoutes extends HttpService {
  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to
                  <i>spray-routing</i>
                  on
                  <i>spray-can</i>
                  !</h1>
              </body>
            </html>
          }
        }
      }
    }
}
