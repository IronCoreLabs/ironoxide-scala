package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkFuture(deviceContext: DeviceContext) extends IronSdk[Future] {
  val underlying = IronSdkSync[IO](deviceContext)

  def groupCreate(options: GroupCreateOpts): Future[GroupMetaResult] =
    underlying.groupCreate(options).unsafeToFuture

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptResult] =
    underlying.documentEncrypt(data, options).unsafeToFuture

  def documentDecrypt(encryptedBytes: ByteVector): Future[DocumentDecryptResult] =
    underlying.documentDecrypt(encryptedBytes).unsafeToFuture

  def advanced: IronSdkAdvanced[Future] = IronSdkAdvancedFuture(deviceContext)
}
