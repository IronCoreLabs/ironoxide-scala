package ironoxide.v1.beta

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{EncryptedData, EncryptedDeks}
import cats.effect.Sync

case class EncryptedBlindIndexSalt(encryptedDeks: EncryptedDeks, encryptedSaltBytes: EncryptedData) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.EncryptedBlindIndexSalt] =
    syncF.delay(new jsdk.EncryptedBlindIndexSalt(encryptedDeks.underlyingBytes, encryptedSaltBytes.underlyingBytes))
}

object EncryptedBlindIndexSalt {
  def apply(ebis: jsdk.EncryptedBlindIndexSalt): EncryptedBlindIndexSalt =
    EncryptedBlindIndexSalt(EncryptedDeks(ebis.getEncryptedDeks), EncryptedData(ebis.getEncryptedSaltBytes))
}
