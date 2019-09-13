package com.ironcorelabs.scala.sdk

private[sdk] object FailedResult {
  def apply(fromJava: com.ironcorelabs.sdk.FailedResult): List[GroupOrUserAccessError] =
    fromJava.getGroups.toList.map(GroupOrUserAccessError.fromGroupError) ++ fromJava.getUsers.toList
      .map(GroupOrUserAccessError.fromUserError)
}
