package ironoxide.v1.user

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * Options that can be specified creating a user.
 *
 * @param needsRotation whether the created user needs their private key rotated
 */
case class UserCreateOpts(needsRotation: Boolean) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.UserCreateOpts] =
    syncF.delay(new jsdk.UserCreateOpts(needsRotation))
}

object UserCreateOpts {
  def apply(): UserCreateOpts = UserCreateOpts(false)
}
