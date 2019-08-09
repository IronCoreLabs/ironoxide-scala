package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import scodec.bits.ByteVector

case class IronSdkSync[F[_]](deviceContext: DeviceContext)(implicit syncF: Sync[F]) extends IronSdk[F] {
  val underlying = deviceContext.toJava.map(com.ironcorelabs.sdk.IronSdk.initialize)

  def groupCreate(options: GroupCreateOpts): F[GroupMetaResult] =
    for {
      javaOpts <- options.toJava
      result   <- underlying.map(_.groupCreate(javaOpts))
    } yield GroupMetaResult(result)

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    for {
      javaOpts <- options.toJava
      result <- underlying.map(
        _.documentEncrypt(
          data.toArray,
          javaOpts
        )
      )
    } yield DocumentEncryptResult(result)

  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult] =
    underlying.map(_.documentDecrypt(encryptedBytes.toArray)).map(DocumentDecryptResult.apply)

  def advanced: IronSdkAdvanced[F] = IronSdkAdvancedSync(deviceContext)
}
