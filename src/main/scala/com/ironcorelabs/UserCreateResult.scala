package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class UserCreateResult(
  userPublicKey: PublicKey,
  needsRotation: Boolean
)

object UserCreateResult {
  def apply(ucr: jsdk.UserCreateResult): UserCreateResult =
    UserCreateResult(
      PublicKey(ucr.getUserPublicKey.asBytes),
      ucr.getNeedsRotation
    )
}
