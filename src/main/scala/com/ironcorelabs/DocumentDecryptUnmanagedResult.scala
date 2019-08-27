package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

/**
 * Result of decrypting an unmanaged document. Includes the id and the decrypted bytes.
 *
 * @param id Unique (within the segment) id of the document
 * @param accessVia User/Group that granted access to the encrypted data. More specifically, this is the user/group associated with the EDEK that was chosen and transformed by the webservice.
 * @param decryptedData The bytes of the decrypted document
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
      ByteVector.view(underlyingBytes)
    )(underlyingBytes)
  }
}
