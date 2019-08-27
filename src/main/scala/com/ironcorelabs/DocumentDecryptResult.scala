package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import java.{util => ju}

/**
 * Result of decrypting a document. Includes minimal metadata as well as the decrypted bytes.
 *
 * @param id Unique (within the segment) ID of the document
 * @param name Non-unique document name
 * @param decryptedData The bytes of the decrypted document
 * @param created When the document was created
 * @param lastUpdated When the document was last updated
 */
case class DocumentDecryptResult(
  id: DocumentId,
  name: Option[DocumentName],
  decryptedData: ByteVector,
  created: ju.Date,
  lastUpdated: ju.Date
)(val underlyingBytes: Array[Byte])

object DocumentDecryptResult {
  def apply(ddr: com.ironcorelabs.sdk.DocumentDecryptResult): DocumentDecryptResult = {
    val underlyingBytes = ddr.getDecryptedData

    DocumentDecryptResult(
      DocumentId(ddr.getId.getId),
      DocumentName.fromJava(ddr.getName),
      ByteVector.view(underlyingBytes),
      ddr.getCreated,
      ddr.getLastUpdated
    )(underlyingBytes)
  }
}
