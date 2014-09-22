organization in ThisBuild := "com.netaporter"

name := "spray-apr"

version := "1.0"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.1"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
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
}
