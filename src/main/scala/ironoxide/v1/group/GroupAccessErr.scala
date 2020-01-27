package ironoxide.v1.group

import com.ironcorelabs.{sdk => jsdk}

/**
 * A single grant/revoke failure for a group
 *
 * @param id group whose access was to be granted/revoked
 * @param err the reason for grant/revoke failure
 */
case class GroupAccessErr(
  id: GroupId,
  err: String
)

object GroupAccessErr {
  def apply(uae: jsdk.GroupAccessErr): GroupAccessErr =
    GroupAccessErr(
      GroupId(uae.getId),
      uae.getErr
    )
}
