package ironoxide.v1.common

import scodec.bits.ByteVector

/**
 * Bytes of encrypted document content.
 */
case class EncryptedData(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedData {
  def apply(bytes: Array[Byte]): EncryptedData = EncryptedData(ByteVector.view(bytes))(bytes)
}
