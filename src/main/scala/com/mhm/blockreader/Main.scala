package com.mhm.blockreader

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.mhm.blockreader.server.BlockReaderRoutes
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

object Main extends App with BlockReaderRoutes with LazyLogging {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher
  implicit val config = ConfigFactory.load()


  val routes = blockReaderRoutes

  Http().bindAndHandle(routes, config.getString("server.host"), config.getInt("server.port")) map { binding =>
    logger.info(s"Server started on port {}", binding.localAddress.getPort)
  } recoverWith { case _ => system.terminate() }

}
