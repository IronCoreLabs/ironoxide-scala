package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import java.{util => ju}

/**
  * Result of decrypting a document. Includes minimal metadata as well as the decrypted bytes.
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
