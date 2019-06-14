package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/**
  * Abbreviated group meta information.
  */
case class GroupMetaResult(
    id: GroupId,
    name: Option[GroupName],
    isAdmin: Boolean,
    isMember: Boolean,
    created: ju.Date,
    lastUpdated: ju.Date
)

object GroupMetaResult {
  def apply(gmr: jsdk.GroupMetaResult): GroupMetaResult = {
    val optName: Option[GroupName] = gmr.getName.toScala.map(n => GroupName(n.getName))

    GroupMetaResult(GroupId(gmr.getId.getId), optName, gmr.isAdmin, gmr.isMember, gmr.getCreated, gmr.getLastUpdated)
  }
}
