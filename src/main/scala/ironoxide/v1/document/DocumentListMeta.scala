package ironoxide.v1.document

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.AssociationType
import java.{util => ju}

/**
 * Single document's (abbreviated) metadata
 *
 * @param id id of the document
 * @param name optional name of the document
 * @param associationType indication of the reason the user can view the document
 * @param created when the document was created
 * @param lastUpdated when the document was last updated
 */
case class DocumentListMeta(
  id: DocumentId,
  name: Option[DocumentName],
  associationType: AssociationType,
  created: ju.Date,
  lastUpdated: ju.Date
)

object DocumentListMeta {
  def apply(dlm: jsdk.DocumentListMeta): DocumentListMeta =
    DocumentListMeta(
      DocumentId(dlm.getId),
      DocumentName(dlm.getName),
      AssociationType(dlm.getAssociationType),
      dlm.getCreated,
      dlm.getLastUpdated
    )
}
