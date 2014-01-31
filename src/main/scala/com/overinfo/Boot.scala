package com.overinfo

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import com.overinfo.models.TwitterAccountModel
import com.overinfo.streams.TwitterStreamer
import com.overinfo.services.{WordsService, TwitterActor}

object Boot extends App {


  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("overcome-news")

  // create and start our service actor
  val service = system.actorOf(Props[WordsService], "twitter-service")

  TwitterAccountModel.getTwitterAccounts map {
    twitterAccount =>
      val accountStream = system.actorOf(Props(new TwitterStreamer), s"account-${twitterAccount.account_id}-stream")
      accountStream ! TwitterStreamer.Start(twitterAccount)
  }
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 3000)
}