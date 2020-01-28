package ironoxide.v1.group

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.OptionalOps
import java.{util => ju}

/**
 * Abbreviated group meta information.
 *
 * @param id unique id of the group (within the segment)
 * @param name None if the group has no name, else the group's name
 * @param isAdmin true if the calling user is a group administrator
 * @param isMember true if the calling user is a group member
 * @param created date and time when the group was created
 * @param lastUpdated date and time when the group was last updated
 * @param needsRotation None if the calling user is not a group admin, else a boolean of if the group's private key needs rotation
 */
case class GroupMetaResult(
  id: GroupId,
  name: Option[GroupName],
  isAdmin: Boolean,
  isMember: Boolean,
  created: ju.Date,
  lastUpdated: ju.Date,
  needsRotation: Option[Boolean]
)

object GroupMetaResult {
  def apply(gmr: jsdk.GroupMetaResult): GroupMetaResult = {
    val optRotation = gmr.getNeedsRotation.toScala.map(_.getBoolean)

    GroupMetaResult(
      GroupId(gmr.getId),
      GroupName(gmr.getName),
      gmr.isAdmin,
      gmr.isMember,
      gmr.getCreated,
      gmr.getLastUpdated,
      optRotation
    )
  }
}
