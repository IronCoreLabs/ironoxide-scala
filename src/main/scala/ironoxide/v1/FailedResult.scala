package ironoxide.v1

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.group.GroupAccessErr
import ironoxide.v1.user.UserAccessErr

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
  def isEmpty: Boolean =
    users.isEmpty && groups.isEmpty
}

object FailedResult {
  def apply(sr: jsdk.FailedResult): FailedResult =
    FailedResult(
      sr.getUsers.toList.map(UserAccessErr(_)),
      sr.getGroups.toList.map(GroupAccessErr(_))
    )
}
