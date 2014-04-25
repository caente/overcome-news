package com.overinfo.services

import spray.http.{AllOrigins, HttpHeaders}
import spray.http.HttpMethods._

/**
 * Created: Miguel A. Iglesias
 * Date: 4/25/14
 */


object Headers {
  val corsHeaders = List(HttpHeaders.`Access-Control-Allow-Origin`(AllOrigins),
    HttpHeaders.`Access-Control-Allow-Methods`(GET, POST, OPTIONS, DELETE),
    HttpHeaders.`Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"))

}

