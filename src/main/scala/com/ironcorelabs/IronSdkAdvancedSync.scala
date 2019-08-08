package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import scodec.bits.ByteVector

case class IronSdkAdvancedSync[F[_]](deviceContext: DeviceContext)(implicit syncF: Sync[F]) extends IronSdkAdvanced[F] {
  val underlying = deviceContext.toJava.map(d => com.ironcorelabs.sdk.IronSdk.initialize(d).advanced)

  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    for {
      javaOpts <- options.toJava
      result <- underlying.map(
        _.documentEncryptUnmanaged(
          data.toArray,
          javaOpts
        )
      )
    } yield DocumentEncryptUnmanagedResult(result)
}
