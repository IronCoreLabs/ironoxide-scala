package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import java.{util => ju}

// unsure about delete/finalize
/**
 * Result for encrypt operations.
 *
 * @param id unique (within the segment) ID of the document
 * @param name non-unique document name
 * @param encryptedData bytes of encrypted document content
 * @param created timestamp for document creation
 * @param lastUpdated timestamp for latest document update
 * @param changed the users and groups whose access was successfully modified
 * @param errors the users and groups whose access failed to be modified
 */
case class DocumentEncryptResult(
  id: DocumentId,
  name: Option[DocumentName],
  encryptedData: ByteVector,
  created: ju.Date,
  lastUpdated: ju.Date,
  changed: List[UserOrGroupId],
  errors: List[GroupOrUserAccessError]
)(val underlyingBytes: Array[Byte])

object DocumentEncryptResult {
  def apply(der: com.ironcorelabs.sdk.DocumentEncryptResult): DocumentEncryptResult = {
    val underlyingBytes = der.getEncryptedData

    DocumentEncryptResult(
      DocumentId(der.getId.getId),
      DocumentName.fromJava(der.getName),
      underlyingBytes,
      der.getCreated,
      der.getLastUpdated,
      succeededResultToScala(der.getChanged),
      failedResultToScala(der.getErrors)
    )
  }

  def apply(
    id: DocumentId,
    name: Option[DocumentName],
    encryptedData: Array[Byte],
    created: ju.Date,
    lastUpdated: ju.Date,
    changed: List[UserOrGroupId],
    errors: List[GroupOrUserAccessError]
  ): DocumentEncryptResult =
    DocumentEncryptResult(id, name, ByteVector.view(encryptedData), created, lastUpdated, changed, errors)(
      encryptedData
    )

}
