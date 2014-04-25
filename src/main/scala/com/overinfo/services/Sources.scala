package com.overinfo.services

import com.overinfo.models.SourcesModel
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.json._
import shapeless.get
import spray.routing.HttpService
import com.overinfo.models.SourcesModel

/**
 * Created: Miguel A. Iglesias
 * Date: 4/22/14
 */
trait Sources extends HttpService {

  import SourcesModel._

  val sources = path("sources") {
    complete(getSources)
  }

  val source = path("sources" / LongNumber) {
    source_id =>
      complete(getSource(source_id))
  }

  val sources_get = source ~ sources
}
