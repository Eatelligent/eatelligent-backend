package com.netaporter.routing

import akka.actor.{Props, Actor}
import com.netaporter._
import spray.routing.{Route, HttpService}
import com.netaporter.core.GetMealsMessageActor
import com.netaporter.clients.{MealClient}

class RestRouting extends HttpService with Actor with PerRequestCreator {

  implicit def actorRefFactory = context

  def receive = runRoute(route)

  val mealService = context.actorOf(Props[MealClient])

  val route = {
    get {
      path("meals") {
        parameters('names) { names =>
          meal {
            println("hei meals")
            GetMealsMessage(names.split(',').toList)
          }
        }
      }
    }
    post {
      path("meals") {
        parameters('names) { names =>
          meal {
            println("post")
            println(names)
            GetMealsMessage(names.split(',').toList)
          }
        }
      }
    }
  }

  def meal(message : RestMessage): Route = {
    println("før")
    ctx => perRequest(ctx, Props(new GetMealsMessageActor(mealService)), message)
    println("etter")
  }
}
