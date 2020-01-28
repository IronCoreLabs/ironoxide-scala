package ironoxide.v1.common

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import scodec.bits.ByteVector

/**
 * Signing private key specific to a device. Used to sign all requests to the IronCore API
 * endpoints. Needed to create a `DeviceContext`. Can be constructed with a ByteVector or Array[Byte]. If Array[Byte]
 * is used, the contained ByteVector will be a view of those bytes.
 */
case class DeviceSigningPrivateKey(bytes: ByteVector) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DeviceSigningKeyPair] =
    syncF.delay(jsdk.DeviceSigningKeyPair.validate(bytes.toArray))
}

object DeviceSigningPrivateKey {
  def apply(bytes: Array[Byte]): DeviceSigningPrivateKey = DeviceSigningPrivateKey(ByteVector.view(bytes))
  def apply(dspk: jsdk.DeviceSigningKeyPair): DeviceSigningPrivateKey = DeviceSigningPrivateKey(dspk.asBytes)
}
