package com.mhm.blockreader.model

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class BlockLabel(height: Int, hash: String, time: Long){
  def isValid = height >= 0
}

object BlockLabel extends ErrorAccumulatingCirceSupport {
  val VoidBlock = BlockLabel(-1, "", 0l)
  val ErrorBlock = BlockLabel(-2, "", 0l)
  implicit val blockLabelDecoder: Decoder[BlockLabel] = deriveDecoder[BlockLabel]
  implicit val blockLabelEncoder: Encoder[BlockLabel] = deriveEncoder[BlockLabel]
}

case class JsonOutput(value: Option[Long]) extends ErrorAccumulatingCirceSupport

object JsonOutput {
  implicit val jsonInputDecoder: Decoder[JsonOutput] = deriveDecoder[JsonOutput]
  implicit val jsonInputEncoder: Encoder[JsonOutput] = deriveEncoder[JsonOutput]
}

case class JsonInput(prev_out: Option[JsonOutput]) extends ErrorAccumulatingCirceSupport

object JsonInput {
  implicit val jsonInputDecoder: Decoder[JsonInput] = deriveDecoder[JsonInput]
  implicit val jsonInputEncoder: Encoder[JsonInput] = deriveEncoder[JsonInput]
}

case class JsonTransaction(
                            inputs: Seq[JsonInput],
                            out: Seq[JsonOutput],
                            tx_index: Long,
                            vin_sz: Int,
                            vout_sz: Int,
                            hash: String,
                            size: Int,
                            time: Long
                          )

object JsonTransaction  extends ErrorAccumulatingCirceSupport {
  implicit val jsonTransactionDecoder: Decoder[JsonTransaction] = deriveDecoder[JsonTransaction]
  implicit val jsonTransactionEncoder: Encoder[JsonTransaction] = deriveEncoder[JsonTransaction]
}

case class JsonBlock(fee: Long, height: Long, n_tx: Int, tx: Seq[JsonTransaction], time: Long)

object JsonBlock extends ErrorAccumulatingCirceSupport {
  val ErrorBlock = JsonBlock(0, -1, 0, Nil, 0l)
  implicit val jsonBlockDecoder: Decoder[JsonBlock] = deriveDecoder[JsonBlock]
  implicit val jsonBlockEncoder: Encoder[JsonBlock] = deriveEncoder[JsonBlock]
}
