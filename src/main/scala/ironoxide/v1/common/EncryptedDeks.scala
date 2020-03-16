package ironoxide.v1.common

import scodec.bits.ByteVector

/**
 * Bytes of encrypted document encryption keys (EDEKs).
 */
case class EncryptedDeks(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedDeks {
  def apply(bytes: Array[Byte]): EncryptedDeks = EncryptedDeks(ByteVector.view(bytes))(bytes)
}
