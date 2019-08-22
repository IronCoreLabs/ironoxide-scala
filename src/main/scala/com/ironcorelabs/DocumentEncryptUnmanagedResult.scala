package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.sdk.{FailedResult, SucceededResult}

// unsure about delete/finalize
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

case class EncryptedData(bytes: ByteVector)(val underlyingBytes: Array[Byte])

case class EncryptedDeks(bytes: ByteVector)(val underlyingBytes: Array[Byte])
