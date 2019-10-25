package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}

case class GroupAccessEditResult(
  succeeded: List[UserId],
  failed: List[GroupAccessEditErr]
)

object GroupAccessEditResult {
  def apply(gaer: jsdk.GroupAccessEditResult): GroupAccessEditResult =
    GroupAccessEditResult(
      gaer.getSucceeded.map(id => UserId(id.getId)).toList,
      gaer.getFailed.map(err => GroupAccessEditErr(UserId(err.getUser), err.getError)).toList
    )
}
