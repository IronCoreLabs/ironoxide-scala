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

  def groupUpdateName(id: GroupId, name: Option[GroupName]): F[GroupMetaResult] =
    for {
      javaId   <- id.toJava
      javaName <- name.orNull.toJava
      result   <- syncF.delay(underlying.groupUpdateName(javaId, javaName))
    } yield GroupMetaResult(result)

  def groupList(): F[List[GroupMetaResult]] =
    for {
      result <- syncF.delay(underlying.groupList.getResult.toList)
    } yield result.map(GroupMetaResult(_))

  def groupDelete(id: GroupId): F[GroupId] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.groupDelete(javaId))
    } yield GroupId(result)

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

  def groupRotatePrivateKey(id: GroupId): F[GroupUpdatePrivateKeyResult] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.groupRotatePrivateKey(javaId))
    } yield GroupUpdatePrivateKeyResult(result)

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    for {
      javaOpts <- options.toJava
      result   <- syncF.delay(underlying.documentEncrypt(data.toArray, javaOpts))
    } yield DocumentEncryptResult(result)

  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult] =
    for {
      result <- syncF.delay(underlying.documentDecrypt(encryptedBytes.toArray))
    } yield DocumentDecryptResult(result)

  def documentList(): F[List[DocumentListMeta]] =
    for {
      result <- syncF.delay(underlying.documentList.getResult.toList)
    } yield result.map(DocumentListMeta(_))

  def documentGetMetadata(id: DocumentId): F[DocumentMetadataResult] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.documentGetMetadata(javaId))
    } yield DocumentMetadataResult(result)

  def documentGetIdFromBytes(encryptedDocument: ByteVector): F[DocumentId] =
    for {
      result <- syncF.delay(underlying.documentGetIdFromBytes(encryptedDocument.toArray))
    } yield DocumentId(result)

  def documentUpdateName(id: DocumentId, name: Option[DocumentName]): F[DocumentMetadataResult] =
    for {
      javaId   <- id.toJava
      javaName <- name.orNull.toJava
      result   <- syncF.delay(underlying.documentUpdateName(javaId, javaName))
    } yield DocumentMetadataResult(result)

  def documentUpdateBytes(id: DocumentId, newDocumentData: ByteVector): F[DocumentEncryptResult] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.documentUpdateBytes(javaId, newDocumentData.toArray))
    } yield DocumentEncryptResult(result)

  def documentGrantAccess(
    documentId: DocumentId,
    userGrants: List[UserId],
    groupGrants: List[GroupId]
  ): F[DocumentAccessResult] =
    for {
      javaId <- documentId.toJava
      users  <- userGrants.traverse(_.toJava).map(_.toArray)
      groups <- groupGrants.traverse(_.toJava).map(_.toArray)
      result <- syncF.delay(underlying.documentGrantAccess(javaId, users, groups))
    } yield DocumentAccessResult(result)

  def documentRevokeAccess(
    documentId: DocumentId,
    userRevokes: List[UserId],
    groupRevokes: List[GroupId]
  ): F[DocumentAccessResult] =
    for {
      javaId <- documentId.toJava
      users  <- userRevokes.traverse(_.toJava).map(_.toArray)
      groups <- groupRevokes.traverse(_.toJava).map(_.toArray)
      result <- syncF.delay(underlying.documentRevokeAccess(javaId, users, groups))
    } yield DocumentAccessResult(result)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): F[UserCreateResult] =
    IronSdk.userCreate(jwt, password, options)

  def userVerify(jwt: String): F[Option[UserResult]] =
    IronSdk.userVerify(jwt)

  def userGetPublicKey(users: List[UserId]): F[List[UserWithKey]] =
    for {
      javaUsers <- users.traverse(_.toJava).map(_.toArray)
      result    <- syncF.delay(underlying.userGetPublicKey(javaUsers).toList)
    } yield result.map(UserWithKey(_))

  def userListDevices(): F[List[UserDevice]] =
    for {
      result <- syncF.delay(underlying.userListDevices.getResult.toList)
    } yield result.map(UserDevice(_))

  def userDeleteDevice(deviceId: Option[DeviceId]): F[DeviceId] =
    for {
      javaId <- deviceId.traverse(_.toJava)
      result <- syncF.delay(underlying.userDeleteDevice(javaId.orNull))
    } yield DeviceId(result)

  def userRotatePrivateKey(password: String): F[UserUpdatePrivateKeyResult] =
    for {
      result <- syncF.delay(underlying.userRotatePrivateKey(password))
    } yield UserUpdatePrivateKeyResult(result)

  def advanced: IronSdkAdvanced[F] = IronSdkAdvancedSync(underlying)
}
