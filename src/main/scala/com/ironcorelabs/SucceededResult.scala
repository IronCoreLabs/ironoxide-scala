package com.ironcorelabs.scala.sdk

private[sdk] object SucceededResult {
  def apply(fromJava: com.ironcorelabs.sdk.SucceededResult): List[UserOrGroupId] =
    fromJava.getUsers().toList.map(UserId(_)) ++ fromJava.getGroups().toList.map(GroupId(_))
}
