package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkAdvancedFuture(underlying: IronSdkAdvanced[IO]) extends IronSdkAdvanced[Future] {

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptUnmanagedResult] =
    underlying.documentEncryptUnmanaged(data, options).unsafeToFuture

  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): Future[DocumentDecryptUnmanagedResult] =
    underlying.documentDecryptUnmanaged(encryptedData, encryptedDeks).unsafeToFuture

}
