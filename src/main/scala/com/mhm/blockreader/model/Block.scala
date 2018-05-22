package com.mhm.blockreader.model

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class LatestBlock(height: Int)

object LatestBlock extends ErrorAccumulatingCirceSupport {
  implicit val latestBlockDecoder: Decoder[LatestBlock] = deriveDecoder[LatestBlock]
  implicit val latestBlockEncoder: Encoder[LatestBlock] = deriveEncoder[LatestBlock]
}