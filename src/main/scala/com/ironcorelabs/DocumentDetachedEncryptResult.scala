package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.sdk.{FailedResult, SucceededResult}

// unsure about delete/finalize
case class DocumentDetachedEncryptResult(
  id: DocumentId,
  encryptedData: ByteVector,
  encryptedDeks: ByteVector,
  changed: SucceededResult,
  errors: FailedResult
)(val underlyingDataBytes: Array[Byte], underlyingDekBytes: Array[Byte])

object DocumentDetachedEncryptResult {
  def apply(eer: com.ironcorelabs.sdk.DocumentDetachedEncryptResult): DocumentDetachedEncryptResult = {
    val underlyingDataBytes = eer.getEncryptedData
    val underlyingDekBytes = eer.getEncryptedDeks

    DocumentDetachedEncryptResult(
      DocumentId(eer.getId.getId),
      ByteVector.view(eer.getEncryptedData),
      ByteVector.view(eer.getEncryptedDeks),
      eer.getChanged,
      eer.getErrors
    )(underlyingDataBytes, underlyingDekBytes)
  }
}
