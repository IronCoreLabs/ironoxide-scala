package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

trait IronSdkAdvanced[F[_]] {

  /** Encrypt the provided document bytes. Return the encrypted document encryption keys (EDEKs) instead of creating a document entry in the IronCore webservice.
   *
   * @param data  bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult]

  /** Encrypt the provided document bytes. Return the encrypted document encryption keys (EDEKs) instead of creating a document entry in the IronCore webservice.
   *
   * @param data  bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncryptUnmanaged(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    documentEncryptUnmanaged(ByteVector.view(data), options)

  /** Decrypt the provided encrypted document with the encrypted document encryption keys (EDEKs).
   *
   * @param encryptedData bytes of encrypted document. Should be the same bytes returned from documentEncryptedUnmanaged
   * @param encryptedDeks encrypted document encryption keys (EDEKs). Should be the same EDEKs returned from documentEncryptUnmanaged
   * @return A [[DocumentDecryptUnmanagedResult]] with the id of the provided document as well as the decrypted document bytes
   */
  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): F[DocumentDecryptUnmanagedResult]

  /** Decrypt the provided encrypted document with the encrypted document encryption keys (EDEKs).
   *
   * @param encryptedData bytes of encrypted document. Should be the same bytes returned from documentEncryptedUnmanaged
   * @param encryptedDeks encrypted document encryption keys (EDEKs). Should be the same EDEKs returned from documentEncryptUnmanaged
   * @return A [[DocumentDecryptUnmanagedResult]] with the id of the provided document as well as the decrypted document bytes
   */
  def documentDecryptUnmanaged(
    encryptedData: Array[Byte],
    encryptedDeks: Array[Byte]
  ): F[DocumentDecryptUnmanagedResult] =
    documentDecryptUnmanaged(
      EncryptedData(ByteVector.view(encryptedData))(encryptedData),
      EncryptedDeks(ByteVector.view(encryptedDeks))(encryptedDeks)
    )

}
