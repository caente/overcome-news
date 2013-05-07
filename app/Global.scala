import actors.TwitterClient.AddTopic
import play.api.GlobalSettings
import utils.Mongo
import actors.ActorStage

object Global extends GlobalSettings {

  override def onStart(application: play.api.Application) { 
    ActorStage.tweetClientSupervisor ! AddTopic("metal")
  } 
   
  override def onStop(application: play.api.Application) { Mongo.connection.close() }
}
