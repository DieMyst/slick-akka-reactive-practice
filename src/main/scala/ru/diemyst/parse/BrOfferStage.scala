package ru.diemyst.parse

import akka.stream.stage.{Context, StageState, StatefulStage, SyncDirective}
import ru.diemyst.schemas.BrShopRow

import scala.io.Source
import scala.xml.pull.{EvElemEnd, EvElemStart, EvText, XMLEvent}
import scala.xml._

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */

object TestObj extends App {
  def processSource[T](input: Source)(f: NodeSeq => T) {
    new scala.xml.parsing.ConstructingParser(input, false) {
      nextch // initialize per documentation
      document // trigger parsing by requesting document

      override def elemStart(pos: Int, pre: String, label: String,
                             attrs: MetaData, scope: NamespaceBinding) {
        super.elemStart(pos, pre, label, attrs, scope)
      }
      override def elemEnd(pos: Int, pre: String, label: String) {
        super.elemEnd(pos, pre, label)
      }
      override def elem(pos: Int, pre: String, label: String, attrs: MetaData,
                        pscope: NamespaceBinding, empty: Boolean, nodes: NodeSeq): NodeSeq = {
        val node = super.elem(pos, pre, label, attrs, pscope, empty, nodes)
        label match {
          case "offer" => f(node); NodeSeq.Empty // process and discard employee nodes
          case _ => node // roll up other nodes
        }
      }
    }
  }

  processSource(scala.io.Source.fromFile("C:\\projects\\slick-akka-reactive-examples\\xml_opts.xml")("cp1251")){ node =>
    // process here
    println(node)
  }
}

class BrOfferStage() extends StatefulStage[XMLEvent, BrShopRow] {
  import ParseImplicits._
  override def initial: StageState[XMLEvent, BrShopRow] = new StageState[XMLEvent, BrShopRow] {

    var brShop: BrShopHelper = BrShopHelper()

    var inUrl = true
    var inPrice = true

    var inOffer: Boolean = false

    override def onPush(elem: XMLEvent, ctx: Context[BrShopRow]): SyncDirective = {
      elem match {
        case EvElemStart(_, "offer", data, _) =>
          inOffer = true
          brShop = BrShopHelper()
          brShop.shopId = data.get("shopId")
          /*available = data.get("available")*/
        case EvElemStart(_, "url", _, _) =>

        case EvElemEnd(pre, label) => println("END: ", pre, label)
        /*case EvText(text) if inPageTitle => lastTitle = text
        case EvText(text) if inPageText && !skipPage => pageText += text*/
        case EvText(text) => println("TEXT: " + text)


      }
      ???
    }

  }

  case class BrShopHelper(var shopId: Option[Int] = None,
                          var available: Option[Boolean] = None,
                          var url: Option[String] = None,
                          var price: Option[Int] = None,
                          var optPrice: Option[Int] = None,
                          var quantity: Option[Int] = None,
                          var cateforyName: Option[String] = None,
                          var categoryId: Option[Int] = None,
                          var picture: Option[String] = None,
                          var vendor: Option[String] = None,
                          var model: Option[String] = None,
                          var name: Option[String] = None,
                          var description: Option[String] = None,
                          var article: Option[String] = None) {
  }

}
