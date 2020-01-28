package ironoxide.v1.group

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{OptionalOps, PublicKey}
import ironoxide.v1.user.UserId
import java.{util => ju}

/**
 * Group information
 *
 * @param id unique id of the group (within the segment)
 * @param name None if the group has no name, else the group's name
 * @param groupMasterPublicKey public key of the group
 * @param isAdmin true if the calling user is a group administrator
 * @param isMember true if the calling user is a group member
 * @param adminList None if the calling user is not in the group, else a list of group admin UserIds
 * @param memberList None if the calling user is not in the group, else a list of group member UserIds
 * @param created date and time when the group was created
 * @param updated date and time when the group was last updated
 * @param needsRotation None if the calling user is not a group admin, else a boolean of if the group's private key needs rotation
 */
case class GroupGetResult(
  id: GroupId,
  name: Option[GroupName],
  groupMasterPublicKey: PublicKey,
  isAdmin: Boolean,
  isMember: Boolean,
  adminList: Option[List[UserId]],
  memberList: Option[List[UserId]],
  created: ju.Date,
  lastUpdated: ju.Date,
  needsRotation: Option[Boolean]
)

object GroupGetResult {
  def apply(ggr: jsdk.GroupGetResult): GroupGetResult = {
    val optRotation = ggr.getNeedsRotation.toScala.map(_.getBoolean)
    val optAdmins = ggr.getAdminList.toScala.map(_.getList.toList.map(UserId(_)))
    val optMembers = ggr.getMemberList.toScala.map(_.getList.toList.map(UserId(_)))

    GroupGetResult(
      GroupId(ggr.getId),
      GroupName(ggr.getName),
      PublicKey(ggr.getGroupMasterPublicKey),
      ggr.isAdmin,
      ggr.isMember,
      optAdmins,
      optMembers,
      ggr.getCreated,
      ggr.getLastUpdated,
      optRotation
    )
  }
}
