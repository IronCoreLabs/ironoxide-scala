package ironoxide.v1

package object common {
  import com.ironcorelabs.{sdk => jsdk}
  import java.{util => ju}

  implicit class OptionalOps[A](o: ju.Optional[A]) {
    def toScala: Option[A] =
      if (o.isPresent)
        Some(o.get)
      else
        None
  }

  private[ironoxide] def failedResultToScala(fromJava: jsdk.FailedResult): List[GroupOrUserAccessError] =
    fromJava.getGroups.toList.map(GroupOrUserAccessError.fromGroupError) ++
      fromJava.getUsers.toList.map(GroupOrUserAccessError.fromUserError)

  private[ironoxide] def succeededResultToScala(fromJava: jsdk.SucceededResult): List[UserOrGroupId] =
    fromJava.getUsers.toList.map(UserId(_)) ++
      fromJava.getGroups.toList.map(GroupId(_))
}
