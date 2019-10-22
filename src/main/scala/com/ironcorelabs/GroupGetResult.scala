package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/**
 * Group information
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
  def apply(gmr: jsdk.GroupGetResult): GroupGetResult = {
    val optName: Option[GroupName] = gmr.getName.toScala.map(n => GroupName(n.getName))
    val optRotation = gmr.getNeedsRotation.toScala.map(_.getBoolean)
    val optAdmins = gmr.getAdminList.toScala.map(_.getList.toList.map(id => UserId(id)))
    val optMembers = gmr.getMemberList.toScala.map(_.getList.toList.map(id => UserId(id)))

    GroupGetResult(
      GroupId(gmr.getId.getId),
      optName,
      PublicKey(gmr.getGroupMasterPublicKey.asBytes),
      gmr.isAdmin,
      gmr.isMember,
      optAdmins,
      optMembers,
      gmr.getCreated,
      gmr.getLastUpdated,
      optRotation
    )
  }
}
