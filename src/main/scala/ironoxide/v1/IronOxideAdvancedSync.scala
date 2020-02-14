package ironoxide.v1

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.document._
import scodec.bits.ByteVector

case class IronOxideAdvancedSync[F[_]](underlying: jsdk.IronOxide)(implicit syncF: Sync[F])
    extends IronOxideAdvanced[F] {

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    for {
      javaOpts <- options.toJava
      result   <- syncF.delay(underlying.advancedDocumentEncryptUnmanaged(data.toArray, javaOpts))
    } yield DocumentEncryptUnmanagedResult(result)

  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): F[DocumentDecryptUnmanagedResult] =
    syncF
      .delay(
        DocumentDecryptUnmanagedResult(
          underlying.advancedDocumentDecryptUnmanaged(encryptedData.underlyingBytes, encryptedDeks.underlyingBytes)
        )
      )
}
