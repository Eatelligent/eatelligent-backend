import sbt._
import Keys._
import play.Play.autoImport._
import PlayKeys._

object Build extends Build {

  val playVersion = "2.3.6"
  val slickPgVersion = "0.6.5.3"
  val playSLickVersion = "0.8.0"

  lazy val appDependencies = Seq(
    jdbc,
    cache,
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
    "com.vividsolutions" % "jts" % "1.13",
    "com.cloudinary" %% "cloudinary-scala-play" % "0.9.4-SNAPSHOT",
    "net.codingwell" %% "scala-guice" % "4.0.0-beta4",
    "org.webjars" %% "webjars-play" % "2.3.0",
    "org.webjars" % "bootstrap" % "3.1.1",
    "org.webjars" % "jquery" % "1.11.0",
    "com.mohiva" %% "play-silhouette" % "1.0"
  )

  lazy val main = Project(id = "play-slick-pg", base = file(".")).enablePlugins(play.PlayScala).settings(
    libraryDependencies ++= appDependencies,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.file("Local Ivy", file(Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),
    scalacOptions ++= Seq("-deprecation", "-feature",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps"
    )
  )

}
