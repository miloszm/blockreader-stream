package com.mhm.blockreader.flow

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, GraphDSL, Sink}
import akka.stream.{ActorMaterializer, FlowShape}
import akka.{Done, NotUsed}
import com.mhm.blockreader.model.{JsonBlock, JsonTransaction}
import com.mhm.blockreader.util.IterableIteratorWrapper.toImmutableIter

import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * converts block stream into transaction stream
  */

object TransactionFlow {
  case class NotUsedRequest(x: Int)

  def create(implicit as: ActorSystem, am: ActorMaterializer):
    Flow[JsonBlock, JsonTransaction, NotUsed] = Flow.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val in = builder.add(Flow[JsonBlock])
    val out = builder.add(Flow[JsonTransaction])

    in.mapConcat[JsonTransaction]{ block =>
      toImmutableIter[JsonTransaction](block.tx)
    } ~> out

    FlowShape.of(in.in, out.out)
  })
}


object TransactionFlowRun extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val sink = Sink.foreach(println)

  val f = BlockLabelSource.create via
    BlockFlow.create via
    TransactionFlow.create runWith
    sink

  Await.result[Done](f, 20.minutes)
}
