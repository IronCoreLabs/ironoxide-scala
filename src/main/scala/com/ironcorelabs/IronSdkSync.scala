package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import cats.implicits._
import scodec.bits.ByteVector

case class IronSdkSync[F[_]](deviceContext: DeviceContext)(implicit syncF: Sync[F]) extends IronSdk[F] {
  val underlying = deviceContext.toJava.map(com.ironcorelabs.sdk.IronSdk.initialize)

  def groupCreate(options: GroupCreateOpts): F[GroupMetaResult] =
    for {
      javaOpts <- options.toJava
      result   <- underlying.map(_.groupCreate(javaOpts))
    } yield GroupMetaResult(result)

  def groupAddMembers(id: GroupId, users: List[UserId]): F[GroupAccessEditResult] =
    for {
      javaId        <- id.toJava
      javaUsersList <- users.traverse(_.toJava)
      javaUsersArray = javaUsersList.toArray[com.ironcorelabs.sdk.UserId]
      result <- underlying.map(_.groupAddMembers(javaId, javaUsersArray))
    } yield GroupAccessEditResult(result)

  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): F[GroupAccessEditResult] =
    for {
      javaId        <- id.toJava
      javaUsersList <- userRevokes.traverse(_.toJava)
      javaUsersArray = javaUsersList.toArray[com.ironcorelabs.sdk.UserId]
      result <- underlying.map(_.groupRemoveMembers(javaId, javaUsersArray))
    } yield GroupAccessEditResult(result)

  def groupGetMetadata(id: GroupId): F[GroupGetResult] =
    for {
      javaId <- id.toJava
      result <- underlying.map(_.groupGetMetadata(javaId))
    } yield GroupGetResult(result)

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    for {
      javaOpts <- options.toJava
      result <- underlying.map(
        _.documentEncrypt(
          data.toArray,
          javaOpts
        )
      )
    } yield DocumentEncryptResult(result)

  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult] =
    underlying.map(_.documentDecrypt(encryptedBytes.toArray)).map(DocumentDecryptResult.apply)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): F[UserCreateResult] =
    IronSdk.userCreate(jwt, password, options)

  def advanced: IronSdkAdvanced[F] = IronSdkAdvancedSync(deviceContext)
}
