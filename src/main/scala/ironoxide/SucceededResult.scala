package ironoxide.v1

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.group.GroupId
import ironoxide.v1.user.UserId

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
