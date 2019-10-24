package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import scodec.bits.ByteVector

case class UserUpdatePrivateKeyResult(
  userKeyId: Long,
  userMasterPrivateKey: EncryptedPrivateKey,
  needsRotation: Boolean
)

object UserUpdatePrivateKeyResult {
  def apply(uupkr: jsdk.UserUpdatePrivateKeyResult): UserUpdatePrivateKeyResult =
    UserUpdatePrivateKeyResult(
      uupkr.getUserKeyId,
      EncryptedPrivateKey(ByteVector(uupkr.getUserMasterPrivateKey.asBytes)),
      uupkr.getNeedsRotation
    )
}
