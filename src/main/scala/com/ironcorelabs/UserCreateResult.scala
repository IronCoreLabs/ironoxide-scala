package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class UserCreateResult(
  userPublicKey: PublicKey,
  needsRotation: Boolean
)

object UserCreateResult {
  def apply(uckp: jsdk.UserCreateResult): UserCreateResult =
    UserCreateResult(
      PublicKey(uckp.getUserPublicKey.asBytes),
      uckp.getNeedsRotation
    )
}
