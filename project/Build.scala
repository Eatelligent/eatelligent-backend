import sbt._
import Keys._
import play.PlayImport.PlayKeys._

object ApplicationBuild extends Build {

  val playVersion = "2.3.4"
  val slickPgVersion = "0.6.5.1"
  val playSLickVersion = "0.8.0"

  lazy val appDependencies = Seq(
    "com.typesafe.play" %% "play-jdbc" % playVersion,
    "com.typesafe.play" %% "play-json" % playVersion,
    "com.typesafe.play" %% "play-slick" % playSLickVersion,
    "com.github.tminglei" %% "slick-pg" % slickPgVersion,
    "com.github.tminglei" %% "slick-pg_joda-time" % slickPgVersion,
    "com.github.tminglei" %% "slick-pg_play-json" % slickPgVersion,
    "com.github.tminglei" %% "slick-pg_jts" % slickPgVersion,
    "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
    "joda-time" % "joda-time" % "2.4",
    "org.joda" % "joda-convert" % "1.6",
    "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
    "com.vividsolutions" % "jts" % "1.13"
  )

  lazy val main = Project(id = "play-slick-pg", base = file(".")).enablePlugins(play.PlayScala).settings(
    libraryDependencies ++= appDependencies,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    scalacOptions ++= Seq("-deprecation", "-feature",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps"
    )
  )
}
