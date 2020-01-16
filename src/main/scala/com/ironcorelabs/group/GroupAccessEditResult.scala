package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import cats.implicits._
import cats.effect.Sync

/**
 * Result from requesting changes to a group's membership or administrators. Partial success is supported.
 *
 * @param succeeded a list of users whose access was modified
 * @param failed a list of users whose access could not be modified
 */
case class GroupAccessEditResult(
  succeeded: List[UserId],
  failed: List[GroupAccessEditErr]
)

object GroupAccessEditResult {
  def apply(gaer: jsdk.GroupAccessEditResult): GroupAccessEditResult =
    GroupAccessEditResult(
      gaer.getSucceeded.toList.map(UserId(_)),
      gaer.getFailed.toList.map(GroupAccessEditErr(_))
    )

  def apply[F[_]](
    id: GroupId,
    users: List[UserId],
    fn: (jsdk.GroupId, Array[jsdk.UserId]) => jsdk.GroupAccessEditResult
  )(implicit syncF: Sync[F]): F[GroupAccessEditResult] =
    for {
      javaId        <- id.toJava
      javaUsersList <- users.traverse(_.toJava)
      javaUsersArray = javaUsersList.toArray[com.ironcorelabs.sdk.UserId]
      result <- syncF.delay(fn(javaId, javaUsersArray))
    } yield GroupAccessEditResult(result)
}
