package ru.diemyst

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.parse.{BrShopParse, RcPlShopParse}
import ru.diemyst.schemas.{Util, DAL, RcPlShopRow}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.xml.Node
import scala.xml.pull._
import slick.driver.PostgresDriver

/**
 * Created by dshakhtarin 
 * Date 15.06.2015
 */
object TestApp extends App {
  implicit val system = ActorSystem()
  implicit val mat = ActorFlowMaterializer()

  try {
    BrShopParse.run(new DAL(PostgresDriver),
      Database.forConfig("mydb"))
  } finally Util.unloadDrivers()
}
