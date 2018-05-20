package com.mhm.server

import akka.http.scaladsl.server.Directives.{complete, get, pathPrefix}
import akka.http.scaladsl.server.Directives._

trait BlockReaderRoutes {

  val feeRoutes = path("fees") {
    get {
      complete("OK")
    }
  }

  lazy val blockReaderRoutes = feeRoutes

}
