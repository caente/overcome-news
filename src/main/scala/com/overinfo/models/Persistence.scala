package com.overinfo.models

import com.mongodb.casbah.MongoClient

/**
 * Created: Miguel A. Iglesias
 * Date: 4/25/14
 */

object Persistence {
  val connection: MongoClient = MongoClient("localhost", 27017)

  val db = connection("overcome-news")
}

trait Persistence {
  val db = Persistence.db
}
