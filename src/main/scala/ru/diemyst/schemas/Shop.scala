package ru.diemyst.schemas

/**
 * Created by DSHakhtarin 
 * Date 16.06.2015
 */
case class ShopRow(id: Option[Int],
                       article: String,
                       name: String,
                       quantity: Option[Int],
                       retailPrice: Option[Double],
                       dealerPrice: Option[Double],
                       photo: Option[String]){}

trait ShopComponent { this: DriverComponent =>
  import driver.api._

  class Shop(tag: Tag) extends Table[ShopRow](tag, "RC_PLANET"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def article = column[String]("article")
    def name = column[String]("name")
    def quantity = column[Int]("quantity", O.Default(0))
    def retailPrice = column[Double]("retail_price", O.Default(0.0))
    def dealerPrice = column[Double]("dealer_price", O.Default(0.0))
    def photo = column[String]("photo")

    override def * = (id.?, article, name, quantity.?, retailPrice.?, dealerPrice.?, photo.?) <> (ShopRow.tupled, ShopRow.unapply)
  }

  val rcPlanet = TableQuery[Shop]

  def insertBatch(batch: Seq[ShopRow]) = {
    rcPlanet ++= batch
  }
}

