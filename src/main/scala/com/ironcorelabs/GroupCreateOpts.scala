package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/**
 * Options for group creation.
 *
 * @param id unique ID of a group within a segment. If None, the server will assign an ID.
 * @param name human readable name of the group. Does not need to be unique.
 * @param addAsMember whether the creating user should be added to the group's membership (in addition to being a group admin)
 * @param needsRotation if true, the group will be marked as needing its private key rotated.
 */
case class GroupCreateOpts(
  id: Option[GroupId],
  name: Option[GroupName],
  addAsMember: Boolean,
  needsRotation: Boolean
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupCreateOpts] =
    for {
      javaId   <- id.traverse(_.toJava).map(_.orNull)
      javaName <- name.traverse(_.toJava).map(_.orNull)
    } yield jsdk.GroupCreateOpts.create(javaId, javaName, addAsMember, needsRotation)
}

object GroupCreateOpts {
  def apply(): GroupCreateOpts = GroupCreateOpts(None, None, true, false)
  def apply(name: GroupName): GroupCreateOpts = GroupCreateOpts(None, Some(name), true, false)
  def apply(id: GroupId): GroupCreateOpts = GroupCreateOpts(Some(id), None, true, false)
  def apply(id: GroupId, name: GroupName): GroupCreateOpts = GroupCreateOpts(Some(id), Some(name), true, false)
}
