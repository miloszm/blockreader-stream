package com.mhm.blockreader.flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.{ActorMaterializer, SourceShape}
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}
import com.mhm.blockreader.model.LatestBlock

import scala.util.{Failure, Success, Try}

object LatestBlockSource {
  def create(implicit as: ActorSystem, am: ActorMaterializer): Source[LatestBlock, NotUsed] = Source.fromGraph(GraphDSL.create() { implicit builder =>
    import GraphDSL.Implicits._

    val source = Source(1 to 5)
    val responseFlow = Flow[(Try[HttpResponse],LatestBlock)].map{
      case (Success(response), _) => LatestBlock(response.toString().length)
      case (Failure(e), lb) => lb
    }
    val poller = Http().cachedHostConnectionPoolHttps[LatestBlock]("blockchain.info")

    val latestBlocks = source.map(i => (HttpRequest(uri = "https://blockchain.info/latestblock"), LatestBlock(i))).via(poller)
    val stream = latestBlocks ~> responseFlow

    SourceShape(stream.outlet)
  })
}


object SourceRun extends App {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  val sink = Sink.foreach(println)
  LatestBlockSource.create.to(sink).run()
}
