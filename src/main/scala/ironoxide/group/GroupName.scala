package ironoxide.v1.group

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.OptionalOps
import java.{util => ju}

/** Group's user-assigned name (non-unique). */
case class GroupName(name: String) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupName] =
    syncF.delay(jsdk.GroupName.validate(name))
}

object GroupName {
  def apply(name: ju.Optional[jsdk.GroupName]): Option[GroupName] =
    name.toScala.map(n => GroupName(n.getName))
}
