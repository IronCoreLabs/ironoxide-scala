package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

case class DocumentDecryptUnmanagedResult(
  id: DocumentId,
  decryptedData: ByteVector
)(val underlyingBytes: Array[Byte])

object DocumentDecryptUnmanagedResult {
  def apply(ddr: com.ironcorelabs.sdk.DocumentDecryptUnmanagedResult): DocumentDecryptUnmanagedResult = {
    val underlyingBytes = ddr.getDecryptedData

    DocumentDecryptUnmanagedResult(
      DocumentId(ddr.getId.getId),
      ByteVector.view(underlyingBytes)
    )(underlyingBytes)
  }
}
