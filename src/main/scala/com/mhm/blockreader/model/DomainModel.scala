package com.mhm.blockreader.model

import com.mhm.blockreader.util.StatCalc


case class Output(value: Long)

case class Input(value: Long)

case class FeeOnlyTransaction(height: Long, index: Int, fees: Long, size: Int, time: Long, blockTime: Long){
  def age: Long = blockTime - time
  def ageInBlocks: Long = age / 600
  def feePerByte: Long = if (size == 0 || fees < 0) 0 else fees / size
}

sealed trait BlockTrait {
  def isEmpty = false
  def tx: Seq[FeeOnlyTransaction]
  def n_tx: Int
  def time: Long
  def fees = tx.map(_.fees).filter(_ > 0)
  val feesSize = if (n_tx > 0) n_tx - 1 else 0
  def sumFees: Long = if (feesSize == 0) 0L else fees.sum
  def avgFee = if (feesSize == 0) 0L else sumFees / feesSize
  def maxFee = if (feesSize == 0) 0L else fees.max
  def minFee = if (feesSize == 0) 0L else fees.min
  def medianFee: Long = StatCalc.median(fees)
}

case class Block(fee: Long, height: Long, n_tx: Int, tx: Seq[FeeOnlyTransaction], time: Long) extends BlockTrait
object EmptyBlock extends BlockTrait {
  def tx = Nil
  def n_tx = 0
  def time = 0
  override def isEmpty = true
}