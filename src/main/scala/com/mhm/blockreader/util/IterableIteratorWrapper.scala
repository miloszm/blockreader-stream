package com.mhm.blockreader.util

object IterableIteratorWrapper {
  implicit class BlockReaderIterator[T](underlying: Iterator[T]) {
    def toIter(): scala.collection.Iterable[T] = {
      underlying.toIterable
    }
  }
  def toImmutableIter[A](elements: Iterable[A]) =
    new scala.collection.immutable.Iterable[A] {
      override def iterator: Iterator[A] = elements.toIterator
    }
}
