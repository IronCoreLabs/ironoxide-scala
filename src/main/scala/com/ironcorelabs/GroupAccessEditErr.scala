package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

/**
 * A failure to edit a group's administrator or membership lists
 */
case class GroupAccessEditErr(
  user: UserId,
  error: String
)

object GroupAccessEditErr {
  def apply(gaee: jsdk.GroupAccessEditErr): GroupAccessEditErr = GroupAccessEditErr(UserId(gaee.getUser), gaee.getError)
}
