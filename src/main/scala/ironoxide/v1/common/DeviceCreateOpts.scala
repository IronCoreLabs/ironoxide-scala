package ironoxide.v1.common

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/** Optional parameters for creating a new device instance. */
case class DeviceCreateOpts(
  name: Option[DeviceName]
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceCreateOpts] =
    for {
      javaName <- name.traverse(_.toJava).map(_.orNull)
    } yield new jsdk.DeviceCreateOpts(javaName)
}

object DeviceCreateOpts {
  def apply(): DeviceCreateOpts = DeviceCreateOpts(None)
  def apply(name: DeviceName): DeviceCreateOpts = DeviceCreateOpts(Some(name))
}
