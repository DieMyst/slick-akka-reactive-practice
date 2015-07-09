package ru.diemyst.parse

import java.net.URL

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import ru.diemyst.schemas.{BrShopRow, DAL}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration._
import scala.xml.XML

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
object BrShopParse {
  def run(dal: DAL, db: Database)(implicit system: ActorSystem, mat: ActorMaterializer): Unit = {
    import ParseImplicits._

    val xml = XML.load(new URL(""))

    val seq = xml \\ "offer"

    Source(seq)
      .map { n =>
        BrShopRow(
          None,
          n.attribute("id"),
          n.attribute("available"),
          (n \ "url").headOption.map(_.text),
          n \ "price",
          n \ "price_opt",
          n \ "quantity",
          (n \ "categoryName").headOption.map(_.text),
          n \ "categoryId",
          (n \ "picture").headOption.map(_.text),
          (n \ "vendor").headOption.map(_.text),
          (n \ "model").headOption.map(_.text),
          (n \ "name").headOption.map(_.text),
          (n \ "description").headOption.map(_.text),
          (n \ "param").find(_.attribute("article").nonEmpty).map(_.text).get
        )
      }.groupedWithin(60, 300.millis)
      .map(seq => db.run(dal.insertBatch(seq)))
      .runWith(Sink.foreach(println))

  }
}
