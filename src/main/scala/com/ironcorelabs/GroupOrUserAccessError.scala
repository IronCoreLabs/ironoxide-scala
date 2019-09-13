package com.ironcorelabs.scala.sdk

case class GroupOrUserAccessError(userOrGroupId: UserOrGroupId, error: String)

object GroupOrUserAccessError {
  def fromGroupError(e: com.ironcorelabs.sdk.GroupAccessErr): GroupOrUserAccessError =
    GroupOrUserAccessError(GroupId(e.getId), e.getErr)
  def fromUserError(e: com.ironcorelabs.sdk.UserAccessErr) = GroupOrUserAccessError(UserId(e.getId), e.getErr)
}
