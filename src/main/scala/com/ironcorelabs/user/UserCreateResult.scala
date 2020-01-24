package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

/**
 * User information from creation
 *
 * @param userPublicKey public key for the user
 * @param needsRotation true if the user's private key needs to be rotated, else false
 */
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
