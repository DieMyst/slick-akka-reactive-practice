package ru.diemyst.schemas

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
case class BrShopRow(id: Option[Int],
                     shopId: Int,
                     available: Boolean,
                     url: Option[String],
                     price: Option[Int],
                     optPrice: Option[Int],
                     quantity: Option[Int],
                     cateforyName: Option[String],
                     categoryId: Option[Int],
                     picture: Option[String],
                     vendor: Option[String],
                     model: Option[String],
                     name: Option[String],
                     description: Option[String],
                     article: String)

trait BrShopComponent { this: DriverComponent =>
  import driver.api._

  /*class BrShop(tag: Tag) extends Table[BrShopRow](tag, "BR_SHOP"){
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def article = column[String]("article")
    def name = column[String]("name")
    def quantity = column[Int]("quantity", O.Default(0))
    def retailPrice = column[Double]("retail_price", O.Default(0.0))
    def dealerPrice = column[Double]("dealer_price", O.Default(0.0))
    def photo = column[String]("photo")

    override def * = (id.?, article, name, quantity.?, retailPrice.?, dealerPrice.?, photo.?) <> (BrShopRow.tupled, BrShopRow.unapply)
  }

  val brShop = TableQuery[BrShop]

  def insertBatch(batch: Seq[BrShopRow]) = {
    brShop ++= batch
  }*/
}
