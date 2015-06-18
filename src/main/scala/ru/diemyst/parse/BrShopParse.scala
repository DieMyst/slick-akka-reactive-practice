package ru.diemyst.parse

import java.io.PipedOutputStream

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.schemas.DAL
import slick.jdbc.JdbcBackend._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.xml.pull.{EvElemStart, XMLEventReader}

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
object BrShopParse {
  def run(dal: DAL, db: Database)(implicit system: ActorSystem, mat: ActorFlowMaterializer): Unit = {
    import ParseImplicits._
    //  val serverConnection = Tcp().outgoingConnection("", 8080)

    //  val getLines = () => scala.io.Source.fromURI(new URI("http://rcplaneta.ru/Excel/files/products_rc.xml"))
    val xmlSrc = scala.io.Source.fromFile("C:\\projects\\slick-akka-reactive-examples\\xml_opts.xml")("cp1251")
    val xmlParse = () => new XMLEventReader(xmlSrc)

    val brShopSource = Source(xmlParse)
      //    .map(seq => db.run(dal.insertBatch(seq)))
      .runWith(Sink.foreach(println))

    Await.result(brShopSource, 10.seconds)
  }
}

/*
.filter {
case EvElemStart(_, "item", data, _) => true
case _ => false
}*/
