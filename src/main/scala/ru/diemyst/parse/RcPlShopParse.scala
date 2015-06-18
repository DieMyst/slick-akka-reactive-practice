package ru.diemyst.parse

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.schemas.{RcPlShopRow, DAL}
import slick.jdbc.JdbcBackend._
import scala.concurrent.duration._

import scala.concurrent.Await
import scala.xml.pull.{EvElemStart, XMLEventReader}

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
object RcPlShopParse {
  def run(dal: DAL, db: Database)(implicit system: ActorSystem, mat: ActorFlowMaterializer): Unit = {
    import ParseImplicits._
    //  val serverConnection = Tcp().outgoingConnection("", 8080)

    //  val getLines = () => scala.io.Source.fromURI(new URI("http://rcplaneta.ru/Excel/files/products_rc.xml"))
    db.run(dal.create())

    val xmlSrc = scala.io.Source.fromURL("http://rcplaneta.ru/Excel/files/products_rc.xml")("utf-8")
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
