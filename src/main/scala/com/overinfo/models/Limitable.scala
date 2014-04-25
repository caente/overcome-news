package com.overinfo.models

import com.mongodb.casbah.Imports._
import scala.Some

/**
 * Created: Miguel A. Iglesias
 * Date: 4/25/14
 */
trait Limitable {

  implicit def toLimitOption[A](list: MongoCollection#CursorType) = new {
    def limitOption(limit: Option[Int]): MongoCollection#CursorType = limit match {
      case None => list
      case Some(0) => list
      case Some(s) => list.limit(s)
    }
  }

  implicit def toLimitOption[A](list: List[A]) = new {
    def limitOption(limit: Option[Int]): List[A] = limit match {
      case None => list
      case Some(0) => list
      case Some(s) => list.take(s)
    }
  }


  implicit def toSkipOption[A](list: MongoCollection#CursorType) = new {
    def skipOption(limit: Option[Int]): MongoCollection#CursorType = limit match {
      case None => list
      case Some(0) => list
      case Some(s) => list.skip(s)
    }
  }

  implicit def toSkipOption[A](list: List[A]) = new {
    def skipOption(limit: Option[Int]): List[A] = limit match {
      case None => list
      case Some(0) => list
      case Some(s) => list.drop(s)
    }
  }

}
