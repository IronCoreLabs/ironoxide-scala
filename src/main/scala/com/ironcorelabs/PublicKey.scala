package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * Represents an asymmetric public key that wraps the underlying bytes
 * of the key. Can be constructed with a ByteVector or Array[Byte]. If Array[Byte] is used, the contained ByteVector
 * will be a view of those bytes.
 */
case class PublicKey(bytes: ByteVector) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.PublicKey] =
    syncF.delay(jsdk.PublicKey.validate(bytes.toArray))
}

object PublicKey {
  def apply(bytes: Array[Byte]): PublicKey = PublicKey(ByteVector.view(bytes))
}
