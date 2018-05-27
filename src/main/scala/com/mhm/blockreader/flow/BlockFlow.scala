package com.mhm.blockreader.flow

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Flow, GraphDSL, Sink}
import akka.stream.{ActorMaterializer, FlowShape}
import akka.{Done, NotUsed}
import com.mhm.blockreader.model._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * converts blocklabel stream into block stream
  */
object BlockFlow {
  case class NotUsedRequest(blockLabel: BlockLabel)

  def create(implicit as: ActorSystem, am: ActorMaterializer):
    Flow[BlockLabel, BlockTrait, NotUsed] = Flow.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val responseFlow: Flow[(Try[HttpResponse], NotUsedRequest), BlockTrait, NotUsed] = Flow[(Try[HttpResponse],NotUsedRequest)].mapAsync(5) {
      case (resultTry: Try[HttpResponse], request: NotUsedRequest) => resultTry match {
        case Success(httpResponse@HttpResponse (StatusCodes.OK, _, _, _)) =>
          Unmarshal(httpResponse).to[JsonBlock].map(_.toBlock)
        case Failure(e) => Future.successful(request.blockLabel)
      }
      case _ => throw new IllegalStateException()
    }
    val getter = Http().cachedHostConnectionPoolHttps[NotUsedRequest]("blockchain.info")
    val blocks = Flow[BlockLabel].map(blockLabel =>
      (HttpRequest(uri = s"https://blockchain.info/rawblock/${blockLabel.hash}"), NotUsedRequest(blockLabel))).via(getter)

    val in = builder.add(Flow[BlockLabel])
    val out = builder.add(Flow[BlockTrait])

    in ~> blocks ~> responseFlow ~> out

    FlowShape.of(in.in, out.out)
  })
}


object BlockFlowRun extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val sink = Sink.foreach(println)

  val f = BlockLabelSource.create via BlockFlow.create runWith sink

  Await.result[Done](f, 20.minutes)
}
