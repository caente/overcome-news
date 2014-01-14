organization := "com.dimeder"

version := "0.1"

scalaVersion := "2.10.3"

name := "overcome-news"


resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaV = "2.1.4"
  val sprayV = "1.1.0"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-routing" % sprayV,
    "io.spray" % "spray-testkit" % sprayV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "org.twitter4j" % "twitter4j-core" % "3.0.5",
    "org.twitter4j" % "twitter4j-stream" % "3.0.5",
    "org.specs2" %% "specs2" % "2.2.3" % "test",
    "org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test"
  )
}

//seq(Revolver.settings: _*)
