package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.EncryptedPrivateKey

/**
 * Result of rotating the user's private key
 *
 * @param userMasterPrivateKey updated encrypted user private key
 * @param needsRotation true if this user's master key requires additonal rotation
 */
case class UserUpdatePrivateKeyResult(
  userMasterPrivateKey: EncryptedPrivateKey,
  needsRotation: Boolean
)

object UserUpdatePrivateKeyResult {
  def apply(uupkr: jsdk.UserUpdatePrivateKeyResult): UserUpdatePrivateKeyResult =
    UserUpdatePrivateKeyResult(EncryptedPrivateKey(uupkr.getUserMasterPrivateKey), uupkr.getNeedsRotation)
}
