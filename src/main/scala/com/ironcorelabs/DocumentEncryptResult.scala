package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import java.{util => ju}
import com.ironcorelabs.sdk.{FailedResult, SucceededResult}

// unsure about delete/finalize
/**
 * Result for encrypt operations.
 *
 * @param id Unique (within the segment) ID of the document
 * @param name Non-unique document name
 * @param encryptedData Bytes of encrypted document content
 * @param created When the document was created
 * @param lastUpdated When the document was last updated
 * @param changed The users and groups whose access was successfully modified
 * @param errors The users and groups whose access failed to be modified
 */
case class DocumentEncryptResult(
  id: DocumentId,
  name: Option[DocumentName],
  encryptedData: ByteVector,
  created: ju.Date,
  lastUpdated: ju.Date,
  changed: SucceededResult,
  errors: FailedResult
)(val underlyingBytes: Array[Byte])

object DocumentEncryptResult {
  def apply(der: com.ironcorelabs.sdk.DocumentEncryptResult): DocumentEncryptResult = {
    val underlyingBytes = der.getEncryptedData

    DocumentEncryptResult(
      DocumentId(der.getId.getId),
      DocumentName.fromJava(der.getName),
      ByteVector.view(der.getEncryptedData),
      der.getCreated,
      der.getLastUpdated,
      der.getChanged,
      der.getErrors
    )(underlyingBytes)
  }
}
