package com.overinfo

import spray.http.HttpMethods._
import spray.http.{AllOrigins, HttpHeaders}

/**
 * Created: Miguel A. Iglesias
 * Date: 1/30/14
 */
package object services {

  val corsHeaders = List(HttpHeaders.`Access-Control-Allow-Origin`(AllOrigins),
    HttpHeaders.`Access-Control-Allow-Methods`(GET, POST, OPTIONS, DELETE),
    HttpHeaders.`Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent"))

}
