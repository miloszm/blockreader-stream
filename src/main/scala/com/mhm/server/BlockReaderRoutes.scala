package com.mhm.server

import akka.http.scaladsl.server.Directives._
import com.mhm.model.FeeResult

trait BlockReaderRoutes {

  val feeRoutes = path("fees") {

    import akka.http.scaladsl.server.Directives._

    get {
      complete{ FeeResult.fake }
    }
  }

  lazy val blockReaderRoutes = feeRoutes

}
