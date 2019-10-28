package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class GroupAccessEditResult(
  succeeded: List[UserId],
  failed: List[GroupAccessEditErr]
)

object GroupAccessEditResult {
  def apply(gaer: jsdk.GroupAccessEditResult): GroupAccessEditResult =
    GroupAccessEditResult(
      gaer.getSucceeded.map(UserId(_)).toList,
      gaer.getFailed.map(GroupAccessEditErr(_)).toList
    )
}
