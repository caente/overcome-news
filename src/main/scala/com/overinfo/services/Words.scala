package com.overinfo.services

import spray.routing.HttpService

/**
 * Created: Miguel A. Iglesias
 * Date: 4/22/14
 */
trait Words extends HttpService {

  val words = path("words") {
    complete("ok")
  }

}
