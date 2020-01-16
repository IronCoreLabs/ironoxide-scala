package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/**
 * Group's user-assigned name (non-unique).
 */
case class GroupName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.GroupName] =
    syncF.delay(jsdk.GroupName.validate(name))
}

object GroupName {
  def apply(name: jsdk.GroupName): GroupName = GroupName(name.getName)
}
