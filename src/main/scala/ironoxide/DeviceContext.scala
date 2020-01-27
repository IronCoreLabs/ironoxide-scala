package ironoxide.v1

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.user.UserId

/**
 * Account's device context. Needed to initialize the SDK with a set of device keys.
 *
 * @param userId ID of a user
 * @param segmentId ID of a segment
 * @param devicePrivateKey asymmetric private key
 * @param signingPrivateKey signing private key specific to a device
 */
case class DeviceContext(
  userId: UserId,
  segmentId: Long,
  devicePrivateKey: PrivateKey,
  signingPrivateKey: DeviceSigningPrivateKey
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceContext] =
    for {
      javaUserId <- userId.toJava
      javaDPK    <- devicePrivateKey.toJava
      javaSPK    <- signingPrivateKey.toJava
    } yield new jsdk.DeviceContext(javaUserId, segmentId, javaDPK, javaSPK)

  def toJsonString[F[_]](implicit syncF: Sync[F]): F[String] =
    toJava[F].map(_.toJsonString)
}

object DeviceContext {
  def apply(context: jsdk.DeviceContext): DeviceContext =
    DeviceContext(
      UserId(context.getAccountId),
      context.getSegmentId,
      PrivateKey(context.getDevicePrivateKey),
      DeviceSigningPrivateKey(context.getSigningPrivateKey)
    )

  def apply(dar: DeviceAddResult): DeviceContext =
    DeviceContext(
      dar.userId,
      dar.segmentId,
      dar.devicePrivateKey,
      dar.signingPrivateKey
    )

  def fromJsonString[F[_]](jsonString: String)(implicit syncF: Sync[F]): F[DeviceContext] =
    syncF.delay(jsdk.DeviceContext.fromJsonString(jsonString)).map { javaContext =>
      DeviceContext(
        UserId(javaContext.getAccountId),
        javaContext.getSegmentId,
        PrivateKey(javaContext.getDevicePrivateKey),
        DeviceSigningPrivateKey(javaContext.getSigningPrivateKey)
      )
    }
}
