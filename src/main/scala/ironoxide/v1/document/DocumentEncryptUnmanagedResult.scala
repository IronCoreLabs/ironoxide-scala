package ironoxide.v1.document

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{GroupOrUserAccessError, UserOrGroupId, failedResultToScala, succeededResultToScala}
import scodec.bits.ByteVector

// unsure about delete/finalize
/**
 * Result for unmanaged encrypt operations.
 *
 * @param id unique (within the segment) ID of the document
 * @param encryptedData bytes of encrypted document content
 * @param encryptedDeks bytes of encrypted document encryption keys (EDEKs)
 * @param changed the users and groups whose access was successfully modified
 * @param errors the users and groups whose access failed to be modified
 */
case class DocumentEncryptUnmanagedResult(
  id: DocumentId,
  encryptedData: EncryptedData,
  encryptedDeks: EncryptedDeks,
  changed: List[UserOrGroupId],
  errors: List[GroupOrUserAccessError]
)

object DocumentEncryptUnmanagedResult {
  def apply(dder: jsdk.DocumentEncryptUnmanagedResult): DocumentEncryptUnmanagedResult =
    DocumentEncryptUnmanagedResult(
      DocumentId(dder.getId),
      EncryptedData(dder.getEncryptedData),
      EncryptedDeks(dder.getEncryptedDeks),
      succeededResultToScala(dder.getChanged),
      failedResultToScala(dder.getErrors)
    )
}

/**
 * Bytes of encrypted document content.
 */
case class EncryptedData(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedData {
  def apply(bytes: Array[Byte]): EncryptedData = EncryptedData(ByteVector.view(bytes))(bytes)
}

/**
 * Bytes of encrypted document encryption keys (EDEKs).
 */
case class EncryptedDeks(bytes: ByteVector)(val underlyingBytes: Array[Byte])

object EncryptedDeks {
  def apply(bytes: Array[Byte]): EncryptedDeks = EncryptedDeks(ByteVector.view(bytes))(bytes)
}
