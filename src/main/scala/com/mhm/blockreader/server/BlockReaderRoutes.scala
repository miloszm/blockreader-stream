package com.mhm.blockreader.server

import akka.http.scaladsl.server.Directives._
import com.mhm.blockreader.model.FeeResult

trait BlockReaderRoutes {

  val feeRoutes = path("fees") {

    import akka.http.scaladsl.server.Directives._

    get {
      complete{ FeeResult.fake }
    }
  }

  lazy val blockReaderRoutes = feeRoutes

}
