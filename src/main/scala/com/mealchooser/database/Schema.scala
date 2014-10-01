package com.mealchooser.database

import MyPostgresDriver.simple._
import com.github.tminglei.slickpg.JsonString
import org.threeten.bp.ZonedDateTime
import spray.http.DateTime
import org.joda.time.LocalDateTime
import spray.json.JsValue
import scala.slick.lifted.{ProvenShape, ForeignKeyQuery}


class Language(tag: Tag) extends Table[(Int, String, String)](tag, "language") {
  def id: Column[Int] = column[Int]("id", O.PrimaryKey)
  def locale: Column[String] = column[String]("locale")
  def name: Column[String] = column[String]("name")

  def * : ProvenShape[(Int, String, String)] =
    (id, locale, name)
}

class Recipe(tag: Tag) extends Table[(Int, String, Option[JsValue], String, Int, Double, String,
  Option[LocalDateTime],
  Option[LocalDateTime])](tag,
  "recipe") {
  def id: Column[Int] = column[Int]("id", O.PrimaryKey)
  def name: Column[String] = column[String]("name")
  def image: Column[Option[JsValue]] = column[Option[JsValue]]("image")
  def description: Column[String] = column[String]("descritption")
  def language: Column[Int] = column[Int]("language")
  def calories: Column[Double] = column[Double]("calories")
  def procedure: Column[String] = column[String]("procedure")
  def created: Column[Option[LocalDateTime]] = column[Option[LocalDateTime]]("created")
  def modified: Column[Option[LocalDateTime]] = column[Option[LocalDateTime]]("modified")

  def * : ProvenShape[(Int, String, Option[JsValue], String, Int, Double, String, Option[LocalDateTime],
    Option[LocalDateTime])] =
    (id, name, image, description, language, calories, procedure, created, modified)  <> (JsonBean.tupled, JsonBean.unapply)
}

