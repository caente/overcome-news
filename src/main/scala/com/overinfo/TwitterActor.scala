package com.overinfo

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._


/**
 * Created: Miguel A. Iglesias
 * Date: 1/9/14
 */
class TwitterActor extends Actor with TwitterRoutes {
  def actorRefFactory = context

  def receive = runRoute(loginTwitter)
}

trait TwitterRoutes extends HttpService {
  val loginTwitter =
    path("twitter") {
      get {
        respondWithMediaType(`application/json`) {
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
    } ~
      pathPrefix("twitter/callback") {
        get {
          respondWithMediaType(`application/json`) {
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
