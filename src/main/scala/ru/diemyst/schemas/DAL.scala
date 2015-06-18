package ru.diemyst.schemas

import slick.driver.JdbcProfile
/**
 * Created by DSHakhtarin 
 * Date 16.06.2015
 */
class DAL(val driver: JdbcProfile)
  extends DriverComponent with RcPlShopComponent {
  import driver.api._

  def create() = {
    DBIO.seq(rcPl.schema.create)
  }
}
