package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

case class UserCreateOpts(
  needsRotation: Boolean
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.UserCreateOpts] =
    syncF.delay(jsdk.UserCreateOpts.create(needsRotation))
}

object UserCreateOpts {
  def apply(): UserCreateOpts = UserCreateOpts(false)
}
