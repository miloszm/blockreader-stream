package com.mhm.blockreader.model

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class LatestBlock(height: Int){
  def isValid = height >= 0
}

object LatestBlock extends ErrorAccumulatingCirceSupport {
  val VoidBlock = LatestBlock(-1)
  val ErrorBlock = LatestBlock(-2)
  implicit val latestBlockDecoder: Decoder[LatestBlock] = deriveDecoder[LatestBlock]
  implicit val latestBlockEncoder: Encoder[LatestBlock] = deriveEncoder[LatestBlock]
}