package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

/**
 * Failure to edit a group's administrator or member lists
 *
 * @param user the user whose access was to be granted/revoked
 * @param error the reason for grant/revoke failure
 */
case class GroupAccessEditErr(
  user: UserId,
  error: String
)

object GroupAccessEditErr {
  def apply(gaee: jsdk.GroupAccessEditErr): GroupAccessEditErr = GroupAccessEditErr(UserId(gaee.getUser), gaee.getError)
}
