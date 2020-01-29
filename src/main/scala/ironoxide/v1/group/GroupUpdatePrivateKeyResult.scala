package ironoxide.v1.group

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.GroupId

/**
 * Group information after private key update
 *
 * @param id id of the group
 * @param needsRotation true if the group's private key requires additonal rotation
 */
case class GroupUpdatePrivateKeyResult(
  id: GroupId,
  needsRotation: Boolean
)

object GroupUpdatePrivateKeyResult {
  def apply(gupkr: jsdk.GroupUpdatePrivateKeyResult): GroupUpdatePrivateKeyResult =
    GroupUpdatePrivateKeyResult(GroupId(gupkr.getId), gupkr.getNeedsRotation)
}
