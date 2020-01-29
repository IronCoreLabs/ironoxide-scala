package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}
import scodec.bits.ByteVector

case class EncryptedPrivateKey(bytes: ByteVector)

object EncryptedPrivateKey {
  def apply(epk: jsdk.EncryptedPrivateKey): EncryptedPrivateKey = EncryptedPrivateKey(ByteVector.view(epk.asBytes))
}
