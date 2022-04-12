package ironoxide.v1

import cats.effect.IO
import ironoxide.v1.common.{EncryptedData, EncryptedDeks}
import ironoxide.v1.document._
import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.unsafe.implicits.global

case class IronOxideAdvancedFuture(underlying: IronOxideAdvanced[IO]) extends IronOxideAdvanced[Future] {

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptUnmanagedResult] =
    underlying.documentEncryptUnmanaged(data, options).unsafeToFuture

  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): Future[DocumentDecryptUnmanagedResult] =
    underlying.documentDecryptUnmanaged(encryptedData, encryptedDeks).unsafeToFuture
}
