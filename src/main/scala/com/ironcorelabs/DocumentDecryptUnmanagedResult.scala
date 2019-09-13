package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

/**
 * Result of decrypting an unmanaged document. Includes the document's ID, the decrypted bytes, and the ID of the user or group that granted access to the encrypted data.
 *
 * @param id unique (within the segment) ID of the document
 * @param accessVia user/group that granted access to the encrypted data. More specifically, this is the user/group associated with the EDEK that was chosen and transformed by the webservice.
 * @param decryptedData bytes of the decrypted document
 */
case class DocumentDecryptUnmanagedResult(
  id: DocumentId,
  accessVia: UserOrGroupId,
  decryptedData: ByteVector
)(val underlyingBytes: Array[Byte])

object DocumentDecryptUnmanagedResult {
  def apply(ddr: com.ironcorelabs.sdk.DocumentDecryptUnmanagedResult): DocumentDecryptUnmanagedResult = {
    val underlyingBytes = ddr.getDecryptedData

    DocumentDecryptUnmanagedResult(
      DocumentId(ddr.getId.getId),
      UserOrGroupId(ddr.getAccessViaUserOrGroup()),
      underlyingBytes
    )
  }

  def apply(
    id: DocumentId,
    accessVia: UserOrGroupId,
    decryptedData: Array[Byte]
  ): DocumentDecryptUnmanagedResult =
    DocumentDecryptUnmanagedResult(id, accessVia, ByteVector.view(decryptedData))(decryptedData)
}
