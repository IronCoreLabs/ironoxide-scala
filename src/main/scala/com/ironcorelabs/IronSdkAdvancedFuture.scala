package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkAdvancedFuture(deviceContext: DeviceContext) extends IronSdkAdvanced[Future] {
  val underlying = IronSdkAdvancedSync[IO](deviceContext)

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptUnmanagedResult] =
    underlying.documentEncryptUnmanaged(data, options).unsafeToFuture

  def documentDecryptUnmanaged(
    encryptedData: ByteVector,
    encryptedDeks: ByteVector
  ): Future[DocumentDecryptUnmanagedResult] =
    underlying.documentDecryptUnmanaged(encryptedData, encryptedDeks).unsafeToFuture

}
