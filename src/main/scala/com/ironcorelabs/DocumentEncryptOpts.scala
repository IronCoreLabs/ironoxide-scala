package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}

/**
 * Options for encrypting a document.
 * @param id            - unique id of a document. If none, the server will assign an id
 * @param name          - human readable name of the document. Does not need to be unique
 * @param grantToAuthor - Flag determining whether to encrypt to the calling user or not. If set to false at least one value must be present in the `grant` lists.
 * @param userGrants    - list of user ids that will be granted access to the document
 * @param groupGrants   - list of group ids that will be granted access to the document
 */
case class DocumentEncryptOpts(
  id: Option[DocumentId],
  name: Option[DocumentName],
  grantToAuthor: Boolean,
  userGrants: List[UserId],
  groupGrants: List[GroupId],
  policy: Option[PolicyGrant]
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentEncryptOpts] =
    for {
      javaId     <- id.traverse(_.toJava)
      javaName   <- name.traverse(_.toJava)
      javaUsers  <- userGrants.traverse(_.toJava).map(_.toArray)
      javaGroups <- groupGrants.traverse(_.toJava).map(_.toArray)
      javaPolicy <- policy.traverse(_.toJava)
    } yield jsdk.DocumentEncryptOpts.create(
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
