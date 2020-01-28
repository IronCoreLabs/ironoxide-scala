package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}

case class GroupOrUserAccessError(userOrGroupId: UserOrGroupId, error: String)

object GroupOrUserAccessError {
  def fromGroupError(e: jsdk.GroupAccessErr): GroupOrUserAccessError =
    GroupOrUserAccessError(GroupId(e.getId), e.getErr)
  def fromUserError(e: jsdk.UserAccessErr): GroupOrUserAccessError =
    GroupOrUserAccessError(UserId(e.getId), e.getErr)
}
