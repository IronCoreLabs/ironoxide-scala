package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}

/**
 * A successful edit of the access list of a document.
 *
 * @param users a list of users whose access was granted/revoked.
 * @param groups a list of groups whose access was granted/revoked.
 */
case class SucceededResult(
  users: List[UserId],
  groups: List[GroupId]
)

object SucceededResult {
  def apply(sr: jsdk.SucceededResult): SucceededResult =
    SucceededResult(
      sr.getUsers.toList.map(UserId(_)),
      sr.getGroups.toList.map(GroupId(_))
    )
}
