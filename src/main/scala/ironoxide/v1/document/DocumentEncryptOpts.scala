package ironoxide.v1.document

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{GroupId, PolicyGrant, UserId}

/**
 * Options for encrypting a document.
 * If `grantToAuthor` is set to false, at least one value must be present in the `userGrants` or `groupGrants` lists.
 *
 * @param id unique ID of a document. If none, the server will assign an ID
 * @param name unencrypted, human readable name of the document. Does not need to be unique
 * @param grantToAuthor flag determining whether to encrypt to the calling user or not.
 * @param userGrants list of user IDs that will be granted access to the document
 * @param groupGrants list of group IDs that will be granted access to the document
 * @param policy policy labels which will be evaluated to determine grants
 */
case class DocumentEncryptOpts(
  id: Option[DocumentId],
  name: Option[DocumentName],
  grantToAuthor: Boolean,
  userGrants: List[UserId],
  groupGrants: List[GroupId],
  policy: Option[PolicyGrant]
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentEncryptOpts] =
    for {
      javaId     <- id.traverse(_.toJava)
      javaName   <- name.traverse(_.toJava)
      javaUsers  <- userGrants.traverse(_.toJava).map(_.toArray)
      javaGroups <- groupGrants.traverse(_.toJava).map(_.toArray)
      javaPolicy <- policy.traverse(_.toJava)
    } yield new jsdk.DocumentEncryptOpts(
      javaId.orNull,
      javaName.orNull,
      grantToAuthor,
      javaUsers,
      javaGroups,
      javaPolicy.orNull
    )
}

object DocumentEncryptOpts {
  def apply(): DocumentEncryptOpts = DocumentEncryptOpts(None, None, true, Nil, Nil, None)
  def apply(userGrants: List[UserId], groupGrants: List[GroupId]): DocumentEncryptOpts =
    DocumentEncryptOpts(None, None, true, userGrants, groupGrants, None)

  def withPolicyGrants(grantToAuthor: Boolean, policyGrant: PolicyGrant): DocumentEncryptOpts =
    DocumentEncryptOpts(None, None, grantToAuthor, Nil, Nil, Some(policyGrant))

  def withExplicitGrants(
    grantToAuthor: Boolean,
    userGrants: List[UserId],
    groupGrants: List[GroupId]
  ): DocumentEncryptOpts =
    DocumentEncryptOpts(None, None, grantToAuthor, userGrants, groupGrants, None)
}
