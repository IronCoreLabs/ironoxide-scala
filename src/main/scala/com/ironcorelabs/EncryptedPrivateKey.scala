package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.{sdk => jsdk}

case class EncryptedPrivateKey(bytes: ByteVector)

object EncryptedPrivateKey {
  def apply(bytes: Array[Byte]): EncryptedPrivateKey = EncryptedPrivateKey(ByteVector.view(bytes))
  def apply(epk: jsdk.EncryptedPrivateKey): EncryptedPrivateKey = EncryptedPrivateKey(epk.asBytes)
}
