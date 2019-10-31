package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * ID of a device. Device IDs are numeric and will always be greater than 0.
 */
case class DeviceId(id: Long) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceId] = syncF.delay(jsdk.DeviceId.validate(id))
}

object DeviceId {
  def apply(id: jsdk.DeviceId): DeviceId = DeviceId(id.getId)
}
