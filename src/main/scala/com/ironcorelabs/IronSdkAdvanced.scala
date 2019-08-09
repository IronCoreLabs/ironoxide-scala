package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

trait IronSdkAdvanced[F[_]] {
  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult]

  def documentEncryptUnmanaged(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    documentEncryptUnmanaged(ByteVector.view(data), options)
}
