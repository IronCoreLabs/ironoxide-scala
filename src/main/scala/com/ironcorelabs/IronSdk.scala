package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

trait IronSdk[F[_]] {

  /** Encrypt the provided document bytes.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult]

  /** Encrypt the provided document bytes.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncrypt(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    documentEncrypt(ByteVector.view(data), options)

  /** Decrypts the provided encrypted document and returns details about the document as well as its decrypted bytes.
   *
   * @param encryptedBytes bytes of encrypted document. Should be the same bytes returned from documentEncrypt
   * @return A [[DocumentDecryptResult]] with metadata about the provided document as well as the decrypted document bytes
   */
  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult]

  /** Decrypts the provided encrypted document and returns details about the document as well as its decrypted bytes.
   *
   * @param encryptedBytes bytes of encrypted document. Should be the same bytes returned from documentEncrypt
   * @return A [[DocumentDecryptResult]] with metadata about the provided document as well as the decrypted document bytes
   */
  def documentDecrypt(encryptedBytes: Array[Byte]): F[DocumentDecryptResult] =
    documentDecrypt(ByteVector.view(encryptedBytes))

  /** Create a group. The creating user will become a group admin.
   *
   * @param options Group creation options. Use `new GroupGreateOpts()` for defaults
   */
  def groupCreate(options: GroupCreateOpts): F[GroupMetaResult]

  /** Access advanced SDK operations.
   *
   * @return an instance of the [[IronSdkAdvanced]]
   */
  def advanced: IronSdkAdvanced[F]
}
