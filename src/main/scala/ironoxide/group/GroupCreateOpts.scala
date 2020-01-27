package ironoxide.v1.group

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.user.UserId

/**
 * Options for group creation.
 *
 * @param id unique ID of a group within a segment. If None, the server will assign an ID.
 * @param name human readable name of the group. Does not need to be unique.
 * @param addAsAdmin if true (default), the creating user will be added as a group admin.
 * @param addAsMember if true (default), the creating user will be added to the group's membership.
 * @param owner if None (default), the creating user will be the owner of the group, else the specified user will be the owner and automatically added as an admin.
 * @param admins list of users to be added as admins of the group. This list takes priority over `addAsAdmin`, so the creating user will be added as an admin even if `addAsAdmin` is false.
 * @param members list of users to be added as members of the group. This list takes priority over `addAsMember`, so the creating user will be added as a member even if `addAsMember` is false.
 * @param needsRotation if true, the group will be marked as needing its private key rotated.
 */
case class GroupCreateOpts(
  id: Option[GroupId],
  name: Option[GroupName],
  addAsAdmin: Boolean,
  addAsMember: Boolean,
  owner: Option[UserId],
  admins: List[UserId],
  members: List[UserId],
  needsRotation: Boolean
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupCreateOpts] =
    for {
      javaId      <- id.traverse(_.toJava).map(_.orNull)
      javaName    <- name.traverse(_.toJava).map(_.orNull)
      javaOwner   <- owner.traverse(_.toJava).map(_.orNull)
      javaAdmins  <- admins.traverse(_.toJava).map(_.toArray)
      javaMembers <- members.traverse(_.toJava).map(_.toArray)
    } yield jsdk.GroupCreateOpts.create(
      javaId,
      javaName,
      addAsAdmin,
      addAsMember,
      javaOwner,
      javaAdmins,
      javaMembers,
      needsRotation
    )
}

object GroupCreateOpts {
  def apply(): GroupCreateOpts = GroupCreateOpts(None, None, true, true, None, Nil, Nil, false)
  def apply(name: GroupName): GroupCreateOpts = GroupCreateOpts(None, Some(name), true, true, None, Nil, Nil, false)
  def apply(id: GroupId): GroupCreateOpts = GroupCreateOpts(Some(id), None, true, true, None, Nil, Nil, false)
  def apply(id: GroupId, name: GroupName): GroupCreateOpts =
    GroupCreateOpts(Some(id), Some(name), true, true, None, Nil, Nil, false)
}
