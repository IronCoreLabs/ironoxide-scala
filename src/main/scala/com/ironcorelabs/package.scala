package com.ironcorelabs.scala

package object sdk {
  import java.{util => ju}

  implicit class OptionalOps[A](o: ju.Optional[A]) {
    def toScala: Option[A] =
      if (o.isPresent)
        Some(o.get)
      else
        None
  }

  private[sdk] def failedResultToScala(fromJava: com.ironcorelabs.sdk.FailedResult): List[GroupOrUserAccessError] =
    fromJava.getGroups.toList.map(GroupOrUserAccessError.fromGroupError) ++ fromJava.getUsers.toList
      .map(GroupOrUserAccessError.fromUserError)

  private[sdk] def succeededResultToScala(fromJava: com.ironcorelabs.sdk.SucceededResult): List[UserOrGroupId] =
    fromJava.getUsers().toList.map(UserId(_)) ++ fromJava.getGroups().toList.map(GroupId(_))

}
