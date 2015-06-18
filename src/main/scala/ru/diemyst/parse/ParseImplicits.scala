package ru.diemyst.parse

import scala.xml.Node

/**
 * Created by DSHakhtarin 
 * Date 18.06.2015
 */
object ParseImplicits {
  implicit def optSeqNode2OptDouble(opt: Option[Seq[Node]]): Option[Double] = {
    opt.flatMap(_.headOption).map(_.text.replace(',', '.').toDouble)
  }

  implicit def optSeqNode2OptInt(opt: Option[Seq[Node]]): Option[Int] = {
    opt.flatMap(_.headOption).map(_.text.toInt)
  }

  implicit def optSeqNode2OptBoolean(opt: Option[Seq[Node]]): Option[Boolean] = {
    opt.flatMap(_.headOption).map(_.text.toBoolean)
  }

  implicit def optSeqNode2OptString(opt: Option[Seq[Node]]): Option[String] = {
    opt.flatMap(_.headOption).map(_.text)
  }

  implicit def optSeqNode2String(opt: Option[Seq[Node]]): String = {
    opt.flatMap(_.headOption).map(_.text).get
  }
}