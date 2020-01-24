package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/** Optional parameters for creating a new device instance. */
case class DeviceCreateOpts(
  name: Option[DeviceName]
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceCreateOpts] =
    for {
      javaName <- name.traverse(_.toJava).map(_.orNull)
    } yield jsdk.DeviceCreateOpts.create(javaName)
}

object DeviceCreateOpts {
  def apply(): DeviceCreateOpts = DeviceCreateOpts(None)
  def apply(name: DeviceName): DeviceCreateOpts = DeviceCreateOpts(Some(name))
}
