package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
  * ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`
  */
case class GroupId(id: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupId] = syncF.delay(jsdk.GroupId.validate(id))
}

object GroupId {
  def apply(gid: jsdk.GroupId): GroupId = GroupId(gid.getId)
}

/**
  * Group's user-assigned name. (non-unique)
  */
case class GroupName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupName] = syncF.delay(jsdk.GroupName.validate(name))
}
