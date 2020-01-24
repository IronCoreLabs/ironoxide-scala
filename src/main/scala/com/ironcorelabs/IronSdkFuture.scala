package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkFuture(underlying: IronSdk[IO]) extends IronSdk[Future] {

  def groupCreate(options: GroupCreateOpts): Future[GroupCreateResult] =
    underlying.groupCreate(options).unsafeToFuture

  def groupUpdateName(id: GroupId, name: Option[GroupName]): Future[GroupMetaResult] =
    underlying.groupUpdateName(id, name).unsafeToFuture

  def groupList: Future[List[GroupMetaResult]] =
    underlying.groupList.unsafeToFuture

  def groupDelete(id: GroupId): Future[GroupId] =
    underlying.groupDelete(id).unsafeToFuture

  def groupAddMembers(id: GroupId, users: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupAddMembers(id, users).unsafeToFuture

  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupRemoveMembers(id, userRevokes).unsafeToFuture

  def groupAddAdmins(id: GroupId, users: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupAddAdmins(id, users).unsafeToFuture

  def groupRemoveAdmins(id: GroupId, userRevokes: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupRemoveAdmins(id, userRevokes).unsafeToFuture

  def groupGetMetadata(id: GroupId): Future[GroupGetResult] = underlying.groupGetMetadata(id).unsafeToFuture

  def documentGetIdFromBytes(encryptedDocument: ByteVector): Future[DocumentId] =
    underlying.documentGetIdFromBytes(encryptedDocument).unsafeToFuture

  def documentUpdateBytes(id: DocumentId, newDocumentData: ByteVector): Future[DocumentEncryptResult] =
    underlying.documentUpdateBytes(id, newDocumentData).unsafeToFuture

  def documentGrantAccess(
    documentId: DocumentId,
    userGrants: List[UserId],
    groupGrants: List[GroupId]
  ): Future[DocumentAccessResult] =
    underlying.documentGrantAccess(documentId, userGrants, groupGrants).unsafeToFuture

  def documentRevokeAccess(
    documentId: DocumentId,
    userRevokes: List[UserId],
    groupRevokes: List[GroupId]
  ): Future[DocumentAccessResult] =
    underlying.documentRevokeAccess(documentId, userRevokes, groupRevokes).unsafeToFuture

  def groupRotatePrivateKey(id: GroupId): Future[GroupUpdatePrivateKeyResult] =
    underlying.groupRotatePrivateKey(id).unsafeToFuture

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptResult] =
    underlying.documentEncrypt(data, options).unsafeToFuture

  def documentDecrypt(encryptedBytes: ByteVector): Future[DocumentDecryptResult] =
    underlying.documentDecrypt(encryptedBytes).unsafeToFuture

  def documentList: Future[List[DocumentListMeta]] =
    underlying.documentList.unsafeToFuture

  def documentGetMetadata(id: DocumentId): Future[DocumentMetadataResult] =
    underlying.documentGetMetadata(id).unsafeToFuture

  def documentUpdateName(id: DocumentId, name: Option[DocumentName]): Future[DocumentMetadataResult] =
    underlying.documentUpdateName(id, name).unsafeToFuture

  def advanced: IronSdkAdvanced[Future] = IronSdkAdvancedFuture(underlying.advanced)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): Future[UserCreateResult] =
    underlying.userCreate(jwt, password, options).unsafeToFuture

  def userVerify(jwt: String): Future[Option[UserResult]] =
    underlying.userVerify(jwt).unsafeToFuture

  def userGetPublicKey(users: List[UserId]): Future[List[UserWithKey]] =
    underlying.userGetPublicKey(users).unsafeToFuture

  def userListDevices: Future[List[UserDevice]] =
    underlying.userListDevices.unsafeToFuture

  def userDeleteDevice(deviceId: Option[DeviceId]): Future[DeviceId] =
    underlying.userDeleteDevice(deviceId).unsafeToFuture

  def userRotatePrivateKey(password: String): Future[UserUpdatePrivateKeyResult] =
    underlying.userRotatePrivateKey(password).unsafeToFuture
}

object IronSdkFuture {
  def initialize(deviceContext: DeviceContext): Future[IronSdk[Future]] =
    IronSdk.initialize[IO](deviceContext).map(IronSdkFuture(_)).unsafeToFuture

  def initializeAndRotate(deviceContext: DeviceContext, password: String): Future[IronSdk[Future]] =
    IronSdk.initializeAndRotate[IO](deviceContext, password).map(IronSdkFuture(_)).unsafeToFuture

  def userCreate[F[_]](jwt: String, password: String, options: UserCreateOpts): Future[UserCreateResult] =
    IronSdk.userCreate[IO](jwt, password, options).unsafeToFuture
}
