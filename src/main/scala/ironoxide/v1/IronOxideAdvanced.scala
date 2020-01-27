package ironoxide.v1

import ironoxide.v1.document._
import scodec.bits.ByteVector

/**
 * Advanced variants of SDK operations for specific use cases. Instantiated with the details
 * of an account's various IDs, devices, and signing keys. Once instantiated, all operations will be
 * performed in the context of the account provided.
 */
trait IronOxideAdvanced[F[_]] {

  /**
   * Encrypts the provided document bytes. Returns the encrypted document encryption keys (EDEKs) instead of creating a document entry in the IronCore webservice.
   * The webservice is still needed for looking up public keys and evaluating policies, but no
   * document is created and the EDEKs are not stored. An additional burden is put on the caller
   * in that the encrypted data AND the EDEKs need to be provided for decryption.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncryptUnmanaged(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult]

  /**
   * Encrypts the provided document bytes. Returns the encrypted document encryption keys (EDEKs) instead of creating a document entry in the IronCore webservice.
   * The webservice is still needed for looking up public keys and evaluating policies, but no
   * document is created and the EDEKs are not stored. An additional burden is put on the caller
   * in that the encrypted data AND the EDEKs need to be provided for decryption.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncryptUnmanaged(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptUnmanagedResult] =
    documentEncryptUnmanaged(ByteVector.view(data), options)

  /**
   * Decrypts the provided encrypted document with the encrypted document encryption keys (EDEKs).
   *
   * @param encryptedData bytes of encrypted document. Should be the same bytes returned from documentEncryptedUnmanaged
   * @param encryptedDeks encrypted document encryption keys (EDEKs). Should be the same EDEKs returned from documentEncryptUnmanaged
   * @return a [[DocumentDecryptUnmanagedResult]] with the ID of the provided document as well as the decrypted document bytes
   */
  def documentDecryptUnmanaged(
    encryptedData: EncryptedData,
    encryptedDeks: EncryptedDeks
  ): F[DocumentDecryptUnmanagedResult]

  /**
   * Decrypts the provided encrypted document with the encrypted document encryption keys (EDEKs).
   *
   * @param encryptedData bytes of encrypted document. Should be the same bytes returned from documentEncryptedUnmanaged
   * @param encryptedDeks encrypted document encryption keys (EDEKs). Should be the same EDEKs returned from documentEncryptUnmanaged
   * @return a [[DocumentDecryptUnmanagedResult]] with the ID of the provided document as well as the decrypted document bytes
   */
  def documentDecryptUnmanaged(
    encryptedData: Array[Byte],
    encryptedDeks: Array[Byte]
  ): F[DocumentDecryptUnmanagedResult] =
    documentDecryptUnmanaged(
      EncryptedData(encryptedData),
      EncryptedDeks(encryptedDeks)
    )

}
