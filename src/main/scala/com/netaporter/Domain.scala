package com.netaporter

// Messages

trait RestMessage

case class GetMealsMessage(petNames: List[String]) extends RestMessage

case class MealsMessage(meals: Seq[Meal]) extends RestMessage


// Domain objects

case class Meal(name: String)

case class Error(message: String)

case class Validation(message: String)

// Exceptions

case object PetOverflowException extends Exception("PetOverflowException: OMG. Pets. Everywhere.")