package com.mhm.blockreader.model

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}


case class JsonBlockLabel(height: Int, hash: String, time: Long){
  def toBlockLabel = BlockLabel(height, hash, time)
}

object JsonBlockLabel extends ErrorAccumulatingCirceSupport {
  implicit val blockLabelDecoder: Decoder[JsonBlockLabel] = deriveDecoder[JsonBlockLabel]
  implicit val blockLabelEncoder: Encoder[JsonBlockLabel] = deriveEncoder[JsonBlockLabel]
}

case class JsonOutput(value: Option[Long]) extends ErrorAccumulatingCirceSupport {
  def toOutput = value.map(v => Output(v))
}

object JsonOutput {
  implicit val jsonInputDecoder: Decoder[JsonOutput] = deriveDecoder[JsonOutput]
  implicit val jsonInputEncoder: Encoder[JsonOutput] = deriveEncoder[JsonOutput]
}

case class JsonInput(prev_out: Option[JsonOutput]) extends ErrorAccumulatingCirceSupport {
  def toInput = for {
    prev <- prev_out
    v <- prev.value
  } yield Input(v)
}

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
                          ){
  def toFeeOnlyTransaction(height: Long, index: Int, blockTime: Long) = {
    val sumInputs: Long = inputs.flatMap(_.toInput).map(_.value).sum
    val sumOutputs: Long = out.flatMap(_.toOutput).map(_.value).sum
    val fees = sumInputs - sumOutputs
    FeeOnlyTransaction(height, index, fees, size, time, blockTime)
  }
}

object JsonTransaction  extends ErrorAccumulatingCirceSupport {
  implicit val jsonTransactionDecoder: Decoder[JsonTransaction] = deriveDecoder[JsonTransaction]
  implicit val jsonTransactionEncoder: Encoder[JsonTransaction] = deriveEncoder[JsonTransaction]
}

case class JsonBlock(fee: Long, height: Int, n_tx: Int, tx: Seq[JsonTransaction], time: Long){
  def toBlock = Block(fee, height, n_tx, tx.zipWithIndex.map{case (transaction, index) => transaction.toFeeOnlyTransaction(height, index, time)}, time)
}

object JsonBlock extends ErrorAccumulatingCirceSupport {
  val ErrorBlock = JsonBlock(0, -1, 0, Nil, 0l)
  implicit val jsonBlockDecoder: Decoder[JsonBlock] = deriveDecoder[JsonBlock]
  implicit val jsonBlockEncoder: Encoder[JsonBlock] = deriveEncoder[JsonBlock]
}
