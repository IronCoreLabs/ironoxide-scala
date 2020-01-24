package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/** Device name type. Validates that the provided name isn't an empty string */
case class DeviceName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceName] =
    syncF.delay(jsdk.DeviceName.validate(name))
}

object DeviceName {
  def apply(name: ju.Optional[jsdk.DeviceName]): Option[DeviceName] =
    name.toScala.map(n => DeviceName(n.getName))
}
