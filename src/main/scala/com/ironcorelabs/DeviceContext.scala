package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/**
 * Account's device context. Needed to initialize the SDK with a set of device keys.
 *
 * @param id ID of a user
 * @param segmentId ID of a segment
 * @param privateKey Asymmetric private key
 * @param deviceSigningKeyPair Signing keypair specific to a device
 */
case class DeviceContext(
  id: UserId,
  segmentId: Long,
  privateKey: PrivateKey,
  deviceSigningKeyPair: DeviceSigningKeyPair
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceContext] =
    for {
      javaId  <- id.toJava
      javaPK  <- privateKey.toJava
      javaSKP <- deviceSigningKeyPair.toJava
    } yield new jsdk.DeviceContext(javaId, segmentId, javaPK, javaSKP)
}
