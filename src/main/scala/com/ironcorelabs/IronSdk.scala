package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

trait IronSdk[F[_]] {
  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult]

  def documentEncrypt(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    documentEncrypt(ByteVector.view(data), options)

  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult]

  def documentDecrypt(encryptedBytes: Array[Byte]): F[DocumentDecryptResult] =
    documentDecrypt(ByteVector.view(encryptedBytes))

  def groupCreate(options: GroupCreateOpts): F[GroupMetaResult]

  def advanced: IronSdkAdvanced[F]
}
