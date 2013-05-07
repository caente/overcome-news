package utils

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import reactivemongo.api.MongoDriver
import reactivemongo.api.gridfs.GridFS
import play.modules.reactivemongo.json.collection.JSONCollection

/** Mongo connection object */
object Mongo {
  val driver = new MongoDriver
  val connection = driver.connection(List("localhost:27017","heroku_app15540830:3gmmfhcf3f0kn31gbqet7djesr@ds063177.mongolab.com:63177/heroku_app15540830"))
  val db = connection("Overcome-News")

  def accessLog: JSONCollection = db.collection[JSONCollection]("accessLog")

  val imagesGridFS = new GridFS(db, "images")
}