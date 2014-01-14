package com.whatson

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._


/**
 * Created: Miguel A. Iglesias
 * Date: 1/9/14
 */
class TwitterActor extends Actor {

}

trait TwitterRoutes extends HttpService {
  val loginTwitter =
    path("twitter") {
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
    } ~
      pathPrefix("twitter/callback") {
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
