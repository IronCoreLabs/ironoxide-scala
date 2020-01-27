package ironoxide.v1

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/** ID of a user or a group. */
sealed trait UserOrGroupId extends Product with Serializable {
  def id: String
}

object UserOrGroupId {
  def apply(uid: jsdk.UserOrGroupId): UserOrGroupId =
    if (uid.isUser)
      ironoxide.v1.user.UserId(uid.getId)
    else
      ironoxide.v1.group.GroupId(uid.getId)
}

package user {

  /** ID of a user. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`. */
  case class UserId(id: String) extends UserOrGroupId {
    private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.UserId] = syncF.delay(jsdk.UserId.validate(id))
  }

  object UserId {
    def apply(uid: jsdk.UserId): UserId = UserId(uid.getId)
  }
}

package group {

  /** ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`. */
  case class GroupId(id: String) extends UserOrGroupId {
    private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupId] =
      syncF.delay(jsdk.GroupId.validate(id))
  }

  object GroupId {
    def apply(gid: jsdk.GroupId): GroupId = GroupId(gid.getId)
  }
}
