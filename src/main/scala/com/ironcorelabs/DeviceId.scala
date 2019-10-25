package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

case class DeviceId(id: Long) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceId] = syncF.delay(jsdk.DeviceId.validate(id))
}

object DeviceId {
  def apply(id: jsdk.DeviceId): DeviceId = DeviceId(id.getId)
}
