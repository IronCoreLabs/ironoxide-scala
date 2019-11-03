package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.{sdk => jsdk}

case class EncryptedPrivateKey(bytes: ByteVector)

object EncryptedPrivateKey {
  def apply(epk: jsdk.EncryptedPrivateKey): EncryptedPrivateKey = EncryptedPrivateKey(ByteVector.view(epk.asBytes))
}
