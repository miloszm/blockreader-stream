package com.mhm.blockreader.flow

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Flow, GraphDSL, Keep, Sink, Source}
import com.mhm.blockreader.model.LatestBlock

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object LatestBlockSource {
  def create(implicit as: ActorSystem, am: ActorMaterializer): Source[LatestBlock, NotUsed] = Source.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    case class NotUsedRequest(x: Int)

    val source = Source.tick[Int](1.seconds, 2.seconds, 1)
    val responseFlow: Flow[(Try[HttpResponse], NotUsedRequest), LatestBlock, NotUsed] = Flow[(Try[HttpResponse],NotUsedRequest)].mapAsync(5) {
      case (resultTry: Try[HttpResponse], _: NotUsedRequest) => resultTry.map {
        case httpResponse @ HttpResponse(StatusCodes.OK, _, _, _) =>
          Unmarshal(httpResponse).to[LatestBlock]
        case _ => throw new IllegalStateException()
      }.get
    }
    val poller = Http().cachedHostConnectionPoolHttps[NotUsedRequest]("blockchain.info")

    val latestBlocks = source.map(i => (HttpRequest(uri = "https://blockchain.info/latestblock"), NotUsedRequest(i))).via(poller)
    val stream = latestBlocks ~> responseFlow

    SourceShape(stream.outlet)
  })
}


object SourceRun extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val sink = Sink.foreach(println)
  val f = LatestBlockSource.create.toMat(sink)(Keep.right).run()
  Await.result[Done](f, 1.minute)
}
