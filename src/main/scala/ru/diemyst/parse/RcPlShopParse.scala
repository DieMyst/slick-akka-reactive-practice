package ru.diemyst.parse

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.schemas.{DAL, RcPlShopRow}
import slick.jdbc.JdbcBackend._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.xml.pull.{EvElemStart, XMLEventReader}

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
object RcPlShopParse {
  def run(dal: DAL, db: Database)(implicit system: ActorSystem, mat: ActorMaterializer): Unit = {
    import ParseImplicits._

    implicit val mat = ActorMaterializer()

    db.run(dal.create())

    val xmlSrc = scala.io.Source.fromURL("")("utf-8")
    val xmlParse = () => new XMLEventReader(xmlSrc)

    val rcPlanetaSource = Source(xmlParse)
      .filter {
      case EvElemStart(_, "item", data, _) => true
      case _ => false
    }.map {
      case EvElemStart(_, _, data, _) =>
        RcPlShopRow(None,
          data.get("article"),
          data.get("name"),
          data.get("qty"),
          data.get("retail_price"),
          data.get("dealer_price"),
          data.get("photo"))
    }.groupedWithin(60, 300.millis)
      .map(seq => db.run(dal.insertBatch(seq)))
      .runWith(Sink.foreach(println))

    Await.result(rcPlanetaSource, 10.seconds)
  }
}
