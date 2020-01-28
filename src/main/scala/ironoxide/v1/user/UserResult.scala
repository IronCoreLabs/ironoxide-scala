package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{OptionalOps, PublicKey}
import java.{util => ju}

/**
 * IDs and public key for existing user on verify result
 *
 * @param userId the user's given id, which uniquely identifies them inside the segment
 * @param segmentId the user's segment id
 * @param userPublicKey the user's public key
 * @param needsRotation true if the private key of the user's keypair needs to be rotated, else false
 */
case class UserResult(
  userId: UserId,
  segmentId: Long,
  userPublicKey: PublicKey,
  needsRotation: Boolean
)

object UserResult {
  def apply(maybe_result: ju.Optional[jsdk.UserResult]): Option[UserResult] =
    maybe_result.toScala.map(result =>
      UserResult(
        UserId(result.getAccountId),
        result.getSegmentId,
        PublicKey(result.getUserPublicKey.asBytes),
        result.getNeedsRotation
      )
    )
}
