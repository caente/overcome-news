organization := "com.overinfo"

version := "0.1"

scalaVersion := "2.10.3"

name := "overcome-news"


resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Repo" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= {
  val akkaV = "2.3.2"
  val sprayV = "1.3.1"
  val twitterV = "4.0.1"
  Seq(
    "io.spray" % "spray-can" % sprayV,
    "io.spray" % "spray-routing" % sprayV,
    "io.spray" % "spray-testkit" % sprayV,
    "com.typesafe.akka" %% "akka-actor" % akkaV force(),
    "com.typesafe.akka" %% "akka-testkit" % akkaV force(),
    "org.twitter4j" % "twitter4j-core" % twitterV,
    "org.twitter4j" % "twitter4j-stream" % twitterV,
    "org.specs2" %% "specs2" % "2.3.11" % "test",
    "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
    "com.netflix.rxjava" % "rxjava-scala" % "0.18.1",
    "org.mongodb" %% "casbah" % "2.7.1-SNAPSHOT",
    "io.spray" %%  "spray-json" % "1.2.6"
  )
}
