package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/**
 * Account's device context. Needed to initialize the SDK with a set of device keys.
 *
 * @param deviceId ID of a device
 * @param userId ID of a user
 * @param segmentId ID of a segment
 * @param devicePrivateKey asymmetric private key
 * @param signingPrivateKey signing keypair specific to a device
 */
case class DeviceContext(
  deviceId: DeviceId,
  userId: UserId,
  segmentId: Long,
  devicePrivateKey: PrivateKey,
  signingPrivateKey: DeviceSigningKeyPair
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceContext] =
    for {
      javaDeviceId <- deviceId.toJava
      javaUserId   <- userId.toJava
      javaDPK      <- devicePrivateKey.toJava
      javaSPK      <- signingPrivateKey.toJava
    } yield new jsdk.DeviceContext(javaDeviceId, javaUserId, segmentId, javaDPK, javaSPK)

  def toJsonString[F[_]](implicit syncF: Sync[F]): F[String] =
    toJava[F].map(_.toJsonString)
}

object DeviceContext {
  def fromJsonString(jsonString: String): DeviceContext = {
    val javaContext = jsdk.DeviceContext.fromJsonString(jsonString)
    DeviceContext(
      DeviceId(javaContext.getDeviceId.getId),
      UserId(javaContext.getAccountId.getId),
      javaContext.getSegmentId,
      PrivateKey(javaContext.getDevicePrivateKey.asBytes),
      DeviceSigningKeyPair(javaContext.getSigningPrivateKey.asBytes)
    )
  }
}
