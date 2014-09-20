organization in ThisBuild := "com.netaporter"

name := "spray-apr"

version := "1.0"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.1.4",
    "io.spray" % "spray-can" % "1.1-M8",
    "io.spray" % "spray-routing" % "1.1-M8",
    "org.json4s" %% "json4s-native" % "3.2.10",
    "com.typesafe.slick" %% "slick" % "2.1.0",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.github.tminglei" %% "slick-pg" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_joda-time" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_jts" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_date2" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_threeten" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_json4s" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_spray-json" % "0.6.3",
    "com.github.tminglei" %% "slick-pg_argonaut" % "0.6.3",
    "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)
