package ironoxide.v1.document

import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}
import scodec.bits.ByteVector

/**
 * Result of decrypting a document. Includes minimal metadata as well as the decrypted bytes.
 *
 * @param id unique (within the segment) ID of the document
 * @param name non-unique document name
 * @param decryptedData bytes of the decrypted document
 * @param created timestamp for document creation
 * @param lastUpdated timestamp for latest document update
 */
case class DocumentDecryptResult(
  id: DocumentId,
  name: Option[DocumentName],
  decryptedData: ByteVector,
  created: ju.Date,
  lastUpdated: ju.Date
)(val underlyingBytes: Array[Byte])

object DocumentDecryptResult {
  def apply(ddr: jsdk.DocumentDecryptResult): DocumentDecryptResult = {
    val underlyingBytes = ddr.getDecryptedData

    DocumentDecryptResult(
      DocumentId(ddr.getId),
      DocumentName(ddr.getName),
      ByteVector.view(underlyingBytes),
      ddr.getCreated,
      ddr.getLastUpdated
    )(underlyingBytes)
  }
}
