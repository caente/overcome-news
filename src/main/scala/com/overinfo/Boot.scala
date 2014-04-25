package com.overinfo

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import com.overinfo.services.OvercomeService

object Boot extends App {


  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("overcome-news")

  // create and start our service actor
  val service = system.actorOf(Props[OvercomeService], "overcome-service")

  IO(Http) ! Http.Bind(service, interface = "0.0.0.0", port = 8081)
}