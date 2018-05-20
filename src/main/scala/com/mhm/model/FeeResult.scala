package com.mhm.model

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class FeeResult
(
  topBlock: Long,
  bottomBlock: Long,
  transactionsLast24h: Int,
  transactionsLast24h01Blocks: Int,
  medianFeePerByteLast24h: Long,
  medianFeePerByteLast24h01Blocks: Long,
  bottomBlock2h: Long,
  transactionsLast2h: Int,
  transactionsLast2h01Blocks: Int,
  medianFeePerByteLast2h: Long,
  medianFeePerByteLast2h01Blocks: Long,
  last2hPeriods: Seq[(String,Long)],
  feePer226BytesSatoshis: Long,
  feePer226BytesBtc: BigDecimal,
  feePer226BytesUsd: BigDecimal,
  feePerByteUsd: BigDecimal,
  emptyBlocksExist: Boolean,
  usdPrice: BigDecimal
)

object FeeResult  extends ErrorAccumulatingCirceSupport {
  def empty = FeeResult(0,0,0,0,0,0,0,0,0,0,0,Nil,0,0,0,0,true,0)
  def fake = FeeResult(
    550000,
    549900,
    160000,
    150000,
    130,
    140,
    549990,
    17000,
    16500,
    145,
    155,
    Seq(
      ("8:00-10:00", 150), ("10:00-12:00", 140), ("12:00-14:00", 130), ("14:00-16:00", 140), ("16:00-18:00", 150), ("18:00-20:00", 140),
      ("20:00-22:00", 120), ("22:00-00:00", 110), ("00:00-02:00", 120), ("02:00-04:00", 125), ("04:00-06:00", 135), ("06:00-08:00", 150)
    ),
    155,
    0.00027,
    2.47,
    0.01,
    true,
    8800)

  implicit val requestDecoder: Decoder[FeeResult] = deriveDecoder[FeeResult]
  implicit val requestEncoder: Encoder[FeeResult] = deriveEncoder[FeeResult]
}