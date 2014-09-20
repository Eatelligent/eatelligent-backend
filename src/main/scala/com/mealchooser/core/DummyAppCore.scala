package com.mealchooser.core

import akka.actor.{ActorRef, Actor}
import com.mealchooser.clients.{MealClient}
import akka.actor.SupervisorStrategy.Escalate
import com.mealchooser._
import MealClient.GetMeals
import MealClient.Meals


import scala.Some
import com.mealchooser.Meal
import akka.actor.OneForOneStrategy

/**
 * The job of this Actor in our application core is to service a request that asks for a list
 * of pets by their names along with their owners.
 *
 * This actor will have the responsibility of making two requests and then aggregating them together:
 *  - One requests for a list the pets by their names
 *  - A separate request for a list of owners by their pet names
 */
class GetMealsMessageActor(mealService: ActorRef) extends Actor {

  var meals = Option.empty[Seq[Meal]]

  def receive = {
    case GetMealsMessage(names) if names.size > 2 => {
      throw PetOverflowException
    }
    case GetMealsMessage(names) => {
      mealService ! GetMeals(names)
      context.become(waitingResponses)
    }
  }

  def waitingResponses: Receive = {
    case Meals(mealSeq) => {
      meals = Some(mealSeq)
      replyIfReady
    }
    case f: Validation => context.parent ! f
  }

  def replyIfReady =
    if(meals.nonEmpty) {
      val petSeq = meals.head

//      val enrichedMeals = petSeq.map{case (hei) => Meal("hei")}

//      val enrichedPets = (petSeq zip ownerSeq).map { case (pet, owner) => pet.withOwner(owner) }
      context.parent ! MealsMessage(petSeq)
    }

  override val supervisorStrategy =
    OneForOneStrategy() {
      case _ => Escalate
    }
}
