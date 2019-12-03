package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import scodec.bits.ByteVector
import com.ironcorelabs.{sdk => jsdk}

case class IronSdkSync[F[_]](underlying: jsdk.IronSdk)(implicit syncF: Sync[F]) extends IronSdk[F] {

  def groupCreate(options: GroupCreateOpts): F[GroupCreateResult] =
    for {
      javaOpts <- options.toJava
      result   <- syncF.delay(underlying.groupCreate(javaOpts))
    } yield GroupCreateResult(result)

  def groupAddMembers(id: GroupId, users: List[UserId]): F[GroupAccessEditResult] =
    GroupAccessEditResult(id, users, underlying.groupAddMembers)

  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): F[GroupAccessEditResult] =
    GroupAccessEditResult(id, userRevokes, underlying.groupRemoveMembers)

  def groupAddAdmins(id: GroupId, users: List[UserId]): F[GroupAccessEditResult] =
    GroupAccessEditResult(id, users, underlying.groupAddAdmins)

  def groupRemoveAdmins(id: GroupId, userRevokes: List[UserId]): F[GroupAccessEditResult] =
    GroupAccessEditResult(id, userRevokes, underlying.groupRemoveAdmins)

  def groupGetMetadata(id: GroupId): F[GroupGetResult] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.groupGetMetadata(javaId))
    } yield GroupGetResult(result)

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    for {
      javaOpts <- options.toJava
      result   <- syncF.delay(underlying.documentEncrypt(data.toArray, javaOpts))
    } yield DocumentEncryptResult(result)

  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult] =
    syncF.delay(underlying.documentDecrypt(encryptedBytes.toArray)).map(DocumentDecryptResult.apply)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): F[UserCreateResult] =
    IronSdk.userCreate(jwt, password, options)

  def userRotatePrivateKey(password: String): F[UserUpdatePrivateKeyResult] =
    syncF.delay(underlying.userRotatePrivateKey(password)).map(UserUpdatePrivateKeyResult.apply)

  def advanced: IronSdkAdvanced[F] = IronSdkAdvancedSync(underlying.advanced)
}
