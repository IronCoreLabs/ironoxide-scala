package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}

/**
 * A single grant/revoke failure for a user
 *
 * @param id user whose access was to be granted/revoked
 * @param err the reason for grant/revoke failure
 */
case class UserAccessErr(
  id: UserId,
  err: String
)

object UserAccessErr {
  def apply(uae: jsdk.UserAccessErr): UserAccessErr =
    UserAccessErr(
      UserId(uae.getId),
      uae.getErr
    )
}
