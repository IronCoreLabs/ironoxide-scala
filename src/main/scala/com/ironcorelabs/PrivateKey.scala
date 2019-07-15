package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * Represents an asymmetric private key that wraps the underlying bytes
 * of the key. Can be constructed with a ByteVector or Array[Byte]. If Array[Byte] is used, the contained ByteVector
 * will be a view of those bytes.
 */
case class PrivateKey(bytes: ByteVector) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.PrivateKey] =
    syncF.delay(jsdk.PrivateKey.validate(bytes.toArray))
}

object PrivateKey {
  def apply(bytes: Array[Byte]): PrivateKey = PrivateKey(ByteVector.view(bytes))
}
