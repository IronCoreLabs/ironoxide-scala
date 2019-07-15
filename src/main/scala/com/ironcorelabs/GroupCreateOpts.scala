package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/**
 * Options for group creation.
 * @param id - unique id of a group within a segment. If None, the server will assign an id.
 * @param name - human readable name of the group. Does not need to be unique.
 * @param addAsMember - creating user will (default, true) or will not (false) be added to the group's membership (in addition to being a group admin)
 */
case class GroupCreateOpts(
  id: Option[GroupId],
  name: Option[GroupName],
  addAsMember: Boolean
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupCreateOpts] =
    for {
      javaId   <- id.traverse(_.toJava).map(_.orNull)
      javaName <- name.traverse(_.toJava).map(_.orNull)
    } yield jsdk.GroupCreateOpts.create(javaId, javaName, addAsMember)
}

object GroupCreateOpts {
  def apply(): GroupCreateOpts = GroupCreateOpts(None, None, true)
  def apply(name: GroupName): GroupCreateOpts = GroupCreateOpts(None, Some(name), true)
  def apply(id: GroupId): GroupCreateOpts = GroupCreateOpts(Some(id), None, true)
  def apply(id: GroupId, name: GroupName): GroupCreateOpts = GroupCreateOpts(Some(id), Some(name), true)
}
