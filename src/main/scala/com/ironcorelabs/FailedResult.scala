package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

/**
 * A failure to edit the access list of a document.
 *
 * @param users a list of users whose access was to be granted/revoked
 * @param groups a list of groups whose access was to be granted/revoked
 */
case class FailedResult(
  users: List[UserAccessErr],
  groups: List[GroupAccessErr]
) {
  def isEmpty(): Boolean =
    users.isEmpty && groups.isEmpty
}

object FailedResult {
  def apply(sr: jsdk.FailedResult): FailedResult =
    FailedResult(
      sr.getUsers.map(UserAccessErr(_)).toList,
      sr.getGroups.map(GroupAccessErr(_)).toList
    )
}
