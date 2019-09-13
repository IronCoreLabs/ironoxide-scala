package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

// unsure about delete/finalize
/**
 * Result for unmanaged encrypt operations.
 *
 * @param id unique (within the segment) ID of the document
 * @param encryptedData bytes of encrypted document content
 * @param encryptedDeks bytes of encrypted document encryption keys (EDEKs)
 * @param changed the users and groups whose access was successfully modified
 * @param errors the users and groups whose access failed to be modified
 */
case class DocumentEncryptUnmanagedResult(
  id: DocumentId,
  encryptedData: EncryptedData,
  encryptedDeks: EncryptedDeks,
  changed: List[UserOrGroupId],
  errors: List[GroupOrUserAccessError]
)

object DocumentEncryptUnmanagedResult {
  def apply(dder: com.ironcorelabs.sdk.DocumentEncryptUnmanagedResult): DocumentEncryptUnmanagedResult = {
    val underlyingDataBytes = dder.getEncryptedData
    val underlyingDekBytes = dder.getEncryptedDeks

    DocumentEncryptUnmanagedResult(
      DocumentId(dder.getId.getId),
      EncryptedData(underlyingDataBytes),
      EncryptedDeks(underlyingDekBytes),
      succeededResultToScala(dder.getChanged),
      failedResultToScala(dder.getErrors)
    )
  }
}

/** Bytes of encrypted document content.
 */
case class EncryptedData(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedData {
  def apply(bytes: Array[Byte]): EncryptedData = EncryptedData(ByteVector.view(bytes))(bytes)
}

/** Bytes of encrypted document encryption keys (EDEKs).
 */
case class EncryptedDeks(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedDeks {
  def apply(bytes: Array[Byte]): EncryptedDeks = EncryptedDeks(ByteVector.view(bytes))(bytes)
}
