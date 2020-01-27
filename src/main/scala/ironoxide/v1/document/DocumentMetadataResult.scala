package ironoxide.v1.document

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.AssociationType
import ironoxide.v1.group.GroupId
import ironoxide.v1.user.UserId
import java.{util => ju}

/**
 * Full metadata for a document
 *
 * @param id id of the document
 * @param name optional name of the document
 * @param associationType indication of the reason the user can view the document
 * @param visibleToUsers list of users who can view the document
 * @param visibleToGroups list of groups that can view the document
 * @param created when the document was created
 * @param lastUpdated when the document was last updated
 */
case class DocumentMetadataResult(
  id: DocumentId,
  name: Option[DocumentName],
  associationType: AssociationType,
  visibleToUsers: List[UserId],
  visibleToGroups: List[GroupId],
  created: ju.Date,
  lastUpdated: ju.Date
)

object DocumentMetadataResult {
  def apply(dmr: jsdk.DocumentMetadataResult): DocumentMetadataResult = {
    val users = dmr.getVisibleToUsers.toList.map(user => UserId(user.getId))
    val groups = dmr.getVisibleToGroups.toList.map(group => GroupId(group.getId))
    DocumentMetadataResult(
      DocumentId(dmr.getId),
      DocumentName(dmr.getName),
      AssociationType(dmr.getAssociationType),
      users,
      groups,
      dmr.getCreated,
      dmr.getLastUpdated
    )
  }
}
