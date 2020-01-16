package com.ironcorelabs.scala.sdk
import com.ironcorelabs.{sdk => jsdk}

/**
 * A user's id and public key
 *
 * @param user the user's id
 * @param publicKey the user's public key
 */
case class UserWithKey(
  user: UserId,
  publicKey: PublicKey
)

object UserWithKey {
  def apply(uwk: jsdk.UserWithKey): UserWithKey =
    UserWithKey(UserId(uwk.getUser), PublicKey(uwk.getPublicKey))
}
