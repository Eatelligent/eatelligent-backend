package com.mealchooser.routing

import akka.actor.{Props, Actor}
import com.mealchooser._
import spray.routing.{Route, HttpService}
import com.mealchooser.core.GetMealsMessageActor
import com.mealchooser.clients.{MealClient}

class RestRouting extends HttpService with Actor with PerRequestCreator {

  implicit def actorRefFactory = context

  def receive = runRoute(route)

  val mealService = context.actorOf(Props[MealClient])

  val route = {
    path("meals") {
      get {
        parameters('names) { names =>
          meal {
            println("hei meals")
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
