package com.mhm.blockreader.server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.mhm.blockreader.model.FeeResult
import org.scalatest.{FunSpec, Matchers}

class BlockReaderRoutesSpec extends FunSpec
  with BlockReaderRoutes
  with Matchers
  with ScalatestRouteTest {

  describe("BlockReaderRoutes") {
    it("return http success for fees") {
      Get("/fees") ~> blockReaderRoutes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[FeeResult] shouldEqual FeeResult.fake
      }
    }
  }

}
