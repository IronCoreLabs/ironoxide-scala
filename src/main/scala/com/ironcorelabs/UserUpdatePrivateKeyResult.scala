package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class UserUpdatePrivateKeyResult(
  userMasterPrivateKey: EncryptedPrivateKey,
  needsRotation: Boolean
)

object UserUpdatePrivateKeyResult {
  def apply(uupkr: jsdk.UserUpdatePrivateKeyResult): UserUpdatePrivateKeyResult =
    UserUpdatePrivateKeyResult(EncryptedPrivateKey(uupkr.getUserMasterPrivateKey), uupkr.getNeedsRotation)
}
