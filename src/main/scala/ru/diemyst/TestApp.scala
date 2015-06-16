package ru.diemyst

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.schemas.{Util, DAL, ShopRow}
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
  def run(dal: DAL, db: Database): Unit = {
    import ParseImplicits._
    import dal.driver.api._
    //  val serverConnection = Tcp().outgoingConnection("", 8080)

    //  val getLines = () => scala.io.Source.fromURI(new URI("http://rcplaneta.ru/Excel/files/products_rc.xml"))

    implicit val system = ActorSystem()
    implicit val mat = ActorFlowMaterializer()

    db.run(dal.create())

    val xmlSrc = scala.io.Source.fromURL("link-to-xml")("utf-8")
    val xmlParse = () => new XMLEventReader(xmlSrc)

    val rcPlanetaSource = Source(xmlParse)
      .filter {
        case EvElemStart(_, "item", data, _) => true
        case _ => false
      }.map {
        case EvElemStart(_, _, data, _) =>
          ShopRow(None,
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

  try {
    run(new DAL(PostgresDriver),
      Database.forConfig("mydb"))
  } finally Util.unloadDrivers()
}



object ParseImplicits {
  implicit def optSeqNode2OptDouble(opt: Option[Seq[Node]]): Option[Double] = {
    opt.flatMap(_.headOption).map(_.text.replace(',', '.').toDouble)
  }

  implicit def optSeqNode2OptInt(opt: Option[Seq[Node]]): Option[Int] = {
    opt.flatMap(_.headOption).map(_.text.toInt)
  }

  implicit def optSeqNode2OptString(opt: Option[Seq[Node]]): Option[String] = {
    opt.flatMap(_.headOption).map(_.text)
  }

  implicit def optSeqNode2String(opt: Option[Seq[Node]]): String = {
    opt.flatMap(_.headOption).map(_.text).get
  }
}
