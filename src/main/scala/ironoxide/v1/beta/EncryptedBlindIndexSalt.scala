package ironoxide.v1.beta

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{EncryptedData, EncryptedDeks}
import cats.effect.Sync
import cats.implicits._

case class EncryptedBlindIndexSalt(encryptedDeks: EncryptedDeks, encryptedSaltBytes: EncryptedData)(
  underlying: jsdk.IronOxide
) {
  def initializeSearch[F[_]](implicit syncF: Sync[F]): F[BlindIndexSearch] =
    toJava.map(jbis => BlindIndexSearch(jbis.initializeSearch(underlying)))

  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.EncryptedBlindIndexSalt] =
    syncF.delay(new jsdk.EncryptedBlindIndexSalt(encryptedDeks.underlyingBytes, encryptedSaltBytes.underlyingBytes))
}

object EncryptedBlindIndexSalt {
  def apply(ebis: jsdk.EncryptedBlindIndexSalt, sdk: jsdk.IronOxide): EncryptedBlindIndexSalt =
    EncryptedBlindIndexSalt(EncryptedDeks(ebis.getEncryptedDeks), EncryptedData(ebis.getEncryptedSaltBytes))(sdk)
}
