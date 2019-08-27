package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.sdk.{FailedResult, SucceededResult}

// unsure about delete/finalize
/**
 * Result for unmanaged encrypt operations.
 *
 * @param id Unique (within the segment) ID of the document
 * @param encryptedData Bytes of encrypted document content
 * @param encryptedDeks Bytes of encrypted document encryption keys (EDEKs)
 * @param changed The users and groups whose access was successfully modified
 * @param errors The users and groups whose access failed to be modified
 */
case class DocumentEncryptUnmanagedResult(
  id: DocumentId,
  encryptedData: EncryptedData,
  encryptedDeks: EncryptedDeks,
  changed: SucceededResult,
  errors: FailedResult
)

object DocumentEncryptUnmanagedResult {
  def apply(dder: com.ironcorelabs.sdk.DocumentEncryptUnmanagedResult): DocumentEncryptUnmanagedResult = {
    val underlyingDataBytes = dder.getEncryptedData
    val underlyingDekBytes = dder.getEncryptedDeks

    DocumentEncryptUnmanagedResult(
      DocumentId(dder.getId.getId),
      EncryptedData(ByteVector.view(dder.getEncryptedData))(underlyingDataBytes),
      EncryptedDeks(ByteVector.view(dder.getEncryptedDeks))(underlyingDekBytes),
      dder.getChanged,
      dder.getErrors
    )
  }
}

/** Bytes of encrypted document content.
 */
case class EncryptedData(bytes: ByteVector)(val underlyingBytes: Array[Byte])

/** Bytes of encrypted document encryption keys (EDEKs).
 */
case class EncryptedDeks(bytes: ByteVector)(val underlyingBytes: Array[Byte])
