package ru.diemyst

import java.net.URL

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import ru.diemyst.parse.BrShopParse
import ru.diemyst.schemas.{DAL, Util}
import slick.driver.PostgresDriver
import slick.jdbc.JdbcBackend.Database

import scala.xml.XML

/**
 * Created by dshakhtarin 
 * Date 15.06.2015
 */
object TestApp extends App {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  try {
    BrShopParse.run(new DAL(PostgresDriver),
      Database.forConfig("mydb"))
  } finally Util.unloadDrivers()
}

object TestApp2 extends App {
  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val xml = XML.load(new URL(""))

  val seq = xml \\ "offer"

  Source(seq).runForeach(println)

}
