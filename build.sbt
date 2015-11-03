name := """etcd-store-play"""

organization := "com.lvxingpai"

version := "0.1.1-SNAPSHOT"

scalaVersion := "2.11.4"

crossScalaVersions := "2.10.4" :: "2.11.4" :: Nil

val morphiaVersion = "1.0.1"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  cache,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.lvxingpai" %% "etcd-store" % "0.4.2"
)

// Uncomment to use Akka
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.11"


//fork in run := true
