package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/**
 * Group information from creation.
 *
 * @param id unique id of the group (within the segment)
 * @param name None if the group has no name, else the group's name
 * @param groupMasterPublicKey public key for encrypting to the group
 * @param isAdmin true if the calling user is a group administrator
 * @param isMember true if the calling user is a group member
 * @param adminList list of group admin UserIds. Group admins can change group membership.
 * @param memberList: list of group member UserIds. Members of a group can decrypt values encrypted to the group.
 * @param created date and time when the group was created
 * @param lastUpdated date and time when the group was last updated
 * @param needsRotation None if the calling user is not a group admin, else a boolean of if the group's private key needs rotation
 */
case class GroupCreateResult(
  id: GroupId,
  name: Option[GroupName],
  groupMasterPublicKey: PublicKey,
  isAdmin: Boolean,
  isMember: Boolean,
  owner: UserId,
  adminList: List[UserId],
  memberList: List[UserId],
  created: ju.Date,
  lastUpdated: ju.Date,
  needsRotation: Option[Boolean]
)

object GroupCreateResult {
  def apply(gcr: jsdk.GroupCreateResult): GroupCreateResult = {
    val optName = gcr.getName.toScala.map(GroupName(_))
    val optRotation = gcr.getNeedsRotation.toScala.map(_.getBoolean)
    val scalaAdminList = gcr.getAdminList.getList.map(UserId(_)).toList
    val scalaMemberList = gcr.getMemberList.getList.map(UserId(_)).toList

    GroupCreateResult(
      GroupId(gcr.getId),
      optName,
      PublicKey(gcr.getGroupMasterPublicKey),
      gcr.isAdmin,
      gcr.isMember,
      UserId(gcr.getOwner),
      scalaAdminList,
      scalaMemberList,
      gcr.getCreated,
      gcr.getLastUpdated,
      optRotation
    )
  }
}
