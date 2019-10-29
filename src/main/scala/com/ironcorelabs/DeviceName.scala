package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * Device name type. Validates that the provided name isn't an empty string
 */
case class DeviceName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceName] =
    syncF.delay(jsdk.DeviceName.validate(name))
}

object DeviceName {
  def apply(name: jsdk.DeviceName): DeviceName = DeviceName(name.getName)
}
