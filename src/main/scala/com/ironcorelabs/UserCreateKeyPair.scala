package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class UserCreateKeyPair(
  userEncryptedMasterKey: PrivateKey,
  userPublicKey: PublicKey,
  needsRotation: Boolean
)

object UserCreateKeyPair {
  def apply(uckp: jsdk.UserCreateKeyPair): UserCreateKeyPair =
    UserCreateKeyPair(
      PrivateKey(uckp.getUserEncryptedMasterKey),
      PublicKey(uckp.getUserPublicKey.asBytes),
      uckp.getNeedsRotation
    )
}
