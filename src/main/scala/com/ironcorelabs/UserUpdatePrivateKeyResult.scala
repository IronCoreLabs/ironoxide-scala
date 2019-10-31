package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import scodec.bits.ByteVector

case class UserUpdatePrivateKeyResult(
  userMasterPrivateKey: EncryptedPrivateKey,
  needsRotation: Boolean
)

object UserUpdatePrivateKeyResult {
  def apply(uupkr: jsdk.UserUpdatePrivateKeyResult): UserUpdatePrivateKeyResult =
    UserUpdatePrivateKeyResult(
      EncryptedPrivateKey(ByteVector(uupkr.getUserMasterPrivateKey.asBytes)),
      uupkr.getNeedsRotation
    )
}
