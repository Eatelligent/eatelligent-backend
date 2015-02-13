import sbt._
import Keys._
import play.Play.autoImport._
import PlayKeys._

object Build extends Build {

  val playVersion = "2.3.8"
  val slickPgVersion = "0.8.1"
  val playSLickVersion = "0.8.1"

  lazy val appDependencies = Seq(
    jdbc,
    cache,
    "com.typesafe.play" %% "play-mailer" % "2.4.0",
    "com.typesafe.play" %% "play-jdbc" % playVersion,
    "com.typesafe.play" %% "play-json" % playVersion,
    "com.typesafe.play" %% "play-slick" % playSLickVersion,
    "com.github.tminglei" %% "slick-pg" % slickPgVersion,
    "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
    "joda-time" % "joda-time" % "2.4",
    "org.joda" % "joda-convert" % "1.6",
    "com.cloudinary" %% "cloudinary-scala-play" % "0.9.7-SNAPSHOT",
    "net.codingwell" %% "scala-guice" % "4.0.0-beta5",
    "com.mohiva" %% "play-silhouette" % "1.0",
    "org.grouplens.lenskit" % "lenskit-all" % "2.1"
  )

  lazy val main = Project(id = "mealchooser-backend", base = file(".")).enablePlugins(play.PlayScala).settings(
    libraryDependencies ++= appDependencies,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.file("Local Ivy", file(Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),
    scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-deprecation", "-feature",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps"
    )
  )

}
