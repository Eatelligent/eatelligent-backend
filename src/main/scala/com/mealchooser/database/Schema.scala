package com.mealchooser.database

import MyPostgresDriver.simple._
import com.github.tminglei.slickpg.JsonString
import org.json4s.native.Json
import spray.http.DateTime
import scala.slick.lifted.{ProvenShape, ForeignKeyQuery}


class Language(tag: Tag) extends Table[(Int, String, String)](tag, "language") {
  def id: Column[Int] = column[Int]("id", O.PrimaryKey)
  def locale: Column[String] = column[String]("locale")
  def name: Column[String] = column[String]("name")

  def * : ProvenShape[(Int, String, String)] =
    (id, locale, name)
}

//class Recipe(tag: Tag) extends Table[(Int, String, JsonString, String, Int, Double, String, DateTime, DateTime)](tag,
//  "recipe") {
//  def id: Column[Int] = column[Int]("id", O.PrimaryKey)
//  def name: Column[String] = column[String]("name")
//  def image: Column[JsonString] = column[JsonString]("image")
//  def description: Column[String] = column[String]("descritption")
//  def language: Column[Int] = column[Int]("language")
//  def calories: Column[Double] = column[Double]("calories")
//  def procedure: Column[String] = column[String]("procedure")
//  def created: Column[DateTime] = column[DateTime]("created")
//  def modified: Column[DateTime] = column[DateTime]("modified")
//
//  def * : ProvenShape[(Int, String, JsonString, String, Int, Double, String, DateTime, DateTime)] =
//    (id, name, image, description, language, calories, procedure, created, modified)
//}

