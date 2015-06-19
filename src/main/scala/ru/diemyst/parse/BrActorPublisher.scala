package ru.diemyst.parse

import akka.actor.ActorLogging
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.Request

import scala.io.Source
import scala.xml._


/**
 * Created by DSHakhtarin 
 * Date 19.06.2015
 */
object BrActorPublisher {
}

class BrActorPublisher(source: Source) extends ActorPublisher[NodeSeq] with ActorLogging {
  import ru.diemyst.parse.BrActorPublisher._

  var trav = generatorToTraversable(processSource(source)).view

  override def receive: Receive = {
    case Request(cnt) =>
//      println(s"Received request to get $cnt data")
      1 == trav
    case m@_ => println(m)
  }

  def sendData() = {
    while(isActive && totalDemand > 0) {
      trav.take(1).foreach(onNext)
      trav = trav.drop(1)
      if (trav.isEmpty) {
        onCompleteThenStop()
      }
    }
  }

  def processSource[T](input: Source)(f: NodeSeq => T) {

    new scala.xml.parsing.ConstructingParser(input, false) {
      nextch() // initialize per documentation

      document()

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

  def generatorToTraversable[T](func: (T => Unit) => Unit) =
    new Traversable[T] {
      def foreach[X](f: T => X) {
        func(f(_))
      }
    }

  def nodes(input: Source): TraversableOnce[NodeSeq] =
    generatorToTraversable(processSource(input))
}
