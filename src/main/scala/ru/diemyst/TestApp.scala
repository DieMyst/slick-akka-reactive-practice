package ru.diemyst

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.parse.{BrShopParse, RcPlShopParse}
import ru.diemyst.schemas.{Util, DAL, RcPlShopRow}
import slick.jdbc.JdbcBackend.Database

import slick.driver.PostgresDriver

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
