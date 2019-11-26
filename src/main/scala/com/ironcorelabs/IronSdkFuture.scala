package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkFuture(underlying: IronSdk[IO]) extends IronSdk[Future] {

  def groupCreate(options: GroupCreateOpts): Future[GroupCreateResult] =
    underlying.groupCreate(options).unsafeToFuture

  def groupAddMembers(id: GroupId, users: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupAddMembers(id, users).unsafeToFuture

  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupRemoveMembers(id, userRevokes).unsafeToFuture

  def groupAddAdmins(id: GroupId, users: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupAddAdmins(id, users).unsafeToFuture

  def groupRemoveAdmins(id: GroupId, userRevokes: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupRemoveAdmins(id, userRevokes).unsafeToFuture

  def groupGetMetadata(id: GroupId): Future[GroupGetResult] = underlying.groupGetMetadata(id).unsafeToFuture

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptResult] =
    underlying.documentEncrypt(data, options).unsafeToFuture

  def documentDecrypt(encryptedBytes: ByteVector): Future[DocumentDecryptResult] =
    underlying.documentDecrypt(encryptedBytes).unsafeToFuture

  def advanced: IronSdkAdvanced[Future] = IronSdkAdvancedFuture(underlying.advanced)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): Future[UserCreateResult] =
    underlying.userCreate(jwt, password, options).unsafeToFuture

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
