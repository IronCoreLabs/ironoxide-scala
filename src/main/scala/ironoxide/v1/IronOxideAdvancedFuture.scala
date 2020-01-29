package ironoxide.v1

import cats.effect.IO
import ironoxide.v1.document._
import scala.concurrent.Future
import scodec.bits.ByteVector

case class IronOxideAdvancedFuture(underlying: IronOxideAdvanced[IO]) extends IronOxideAdvanced[Future] {

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptUnmanagedResult] =
    underlying.documentEncryptUnmanaged(data, options).unsafeToFuture

  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): Future[DocumentDecryptUnmanagedResult] =
    underlying.documentDecryptUnmanaged(encryptedData, encryptedDeks).unsafeToFuture

}
