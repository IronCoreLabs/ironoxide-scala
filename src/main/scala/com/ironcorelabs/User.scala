package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * ID of a user. Unique with in a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`.
 */
case class UserId(id: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.UserId] = syncF.delay(jsdk.UserId.validate(id))
}

object UserId {
  def apply(uid: com.ironcorelabs.sdk.UserId): UserId = UserId(uid.getId)
}
