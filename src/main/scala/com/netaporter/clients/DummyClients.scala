package com.netaporter.clients

import akka.actor.Actor
import com.netaporter._
import com.netaporter.clients.MealClient._
//import com.netaporter.clients.OwnerClient._



/**
 * This could be:
 * - a REST API we are the client for
 * - a Database
 * - anything else that requires IO
 */
class MealClient extends Actor {
  def receive = {
    case GetMeals("Lasagne" :: _) => {
      println("lasagne")
      sender ! Validation("Mener du en lasser?!")
    }
    case GetMeals("Taccer" :: _) => {
      println("taccer")
      ()
    } // Never send a response. Tortoises are too slow
    case GetMeals(mealNames) => {
      println("a name")
      sender ! Meals(mealNames.map(Meal.apply))
    }
  }
}

object MealClient {

  case class GetMeals(mealNames: List[String])

  case class Meals(meals: Seq[Meal])

}

///**
// * This could be:
// * - a REST API we are the client for
// * - a Database
// * - anything else that requires IO
// */
//class OwnerClient extends Actor {
//  def receive = {
//    case GetOwnersForPets(petNames) => {
//      val owners = petNames map {
//        case "Lassie" => Owner("Jeff Morrow")
//        case "Brian Griffin" => Owner("Peter Griffin")
//        case "Tweety" => Owner("Granny")
//        case _ => Owner("Jeff") // Jeff has a lot of pets
//      }
//      sender ! OwnersForPets(owners)
//    }
//  }
//}
//
//object OwnerClient {
//
//  case class GetOwnersForPets(petNames: Seq[String])
//
//  case class OwnersForPets(owners: Seq[Owner])
//
//}
