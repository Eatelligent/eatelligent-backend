package com.mealchooser

import akka.io.IO
import com.mealchooser.database.{Language, MyPostgresDriver}
import spray.can.Http

import MyPostgresDriver.simple._

import akka.actor.{Props, ActorSystem}
import com.mealchooser.routing.RestRouting

object Boot extends App {
  implicit val system = ActorSystem("apr-demo")


  val db = Database.forURL("jdbc:postgresql://localhost/mealchooser", driver = "org.postgresql.Driver")
  db.withSession { implicit session =>
    val languages = TableQuery[Language]
    languages foreach { case (id, locale, name) =>
        println("  " + name + "  " + id)
    }
    println("hello database")
    println(languages)
  }



  val serviceActor = system.actorOf(Props(new RestRouting), name = "rest-routing")

  system.registerOnTermination {
    system.log.info("Actor per request demo shutdown.")
  }

  IO(Http) ! Http.Bind(serviceActor, "localhost", port = 38080)
}