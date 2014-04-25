package com.overinfo.services

import spray.routing.HttpService
import com.overinfo.models.WordsModel._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.httpx.unmarshalling._
import spray.json._
import DefaultJsonProtocol._

/**
 * Created: Miguel A. Iglesias
 * Date: 4/22/14
 */
trait Words extends HttpService {



  case class SourceList(sources: List[Long], limit: Option[Int])

  implicit val sourcesFormat = jsonFormat2(SourceList)

  val words_intersect =
      post {
        path("words" / "intersect") {
          entity(as[SourceList]) {
            case SourceList(sources, limit) =>
              complete(mergeSources(sources, limit))
          }
        }
      }


}
