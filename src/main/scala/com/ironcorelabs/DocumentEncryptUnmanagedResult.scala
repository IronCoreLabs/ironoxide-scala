package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.sdk.{FailedResult, SucceededResult}

// unsure about delete/finalize
case class DocumentEncryptUnmanagedResult(
  id: DocumentId,
  encryptedData: ByteVector,
  encryptedDeks: ByteVector,
  changed: SucceededResult,
  errors: FailedResult
)(val underlyingDataBytes: Array[Byte], underlyingDekBytes: Array[Byte])

object DocumentEncryptUnmanagedResult {
  def apply(dder: com.ironcorelabs.sdk.DocumentEncryptUnmanagedResult): DocumentEncryptUnmanagedResult = {
    val underlyingDataBytes = dder.getEncryptedData
    val underlyingDekBytes = dder.getEncryptedDeks

    DocumentEncryptUnmanagedResult(
      DocumentId(dder.getId.getId),
      ByteVector.view(dder.getEncryptedData),
      ByteVector.view(dder.getEncryptedDeks),
      dder.getChanged,
      dder.getErrors
    )(underlyingDataBytes, underlyingDekBytes)
  }
}
