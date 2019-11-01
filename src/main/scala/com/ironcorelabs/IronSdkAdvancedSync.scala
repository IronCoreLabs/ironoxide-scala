package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import scodec.bits.ByteVector
import com.ironcorelabs.{sdk => jsdk}

case class IronSdkAdvancedSync[F[_]](underlying: jsdk.IronSdkAdvanced)(implicit syncF: Sync[F])
    extends IronSdkAdvanced[F] {

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    for {
      javaOpts <- options.toJava
      result   <- syncF.delay(underlying.documentEncryptUnmanaged(data.toArray, javaOpts))
    } yield DocumentEncryptUnmanagedResult(result)

  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): F[DocumentDecryptUnmanagedResult] =
    syncF
      .delay(underlying.documentDecryptUnmanaged(encryptedData.underlyingBytes, encryptedDeks.underlyingBytes))
      .map(DocumentDecryptUnmanagedResult.apply)
}
