package com.overinfo

import com.mongodb.casbah.MongoConnection

/**
 * Created: Miguel A. Iglesias
 * Date: 1/25/14
 */
package object models {
  val connection: MongoConnection = MongoConnection("localhost", 27017)

  val db = connection("overcome-news")
}
