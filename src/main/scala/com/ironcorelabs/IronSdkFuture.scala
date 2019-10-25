package com.ironcorelabs.scala.sdk

import scala.concurrent.Future
import scodec.bits.ByteVector
import cats.effect.IO

case class IronSdkFuture(deviceContext: DeviceContext) extends IronSdk[Future] {
  val underlying = IronSdkSync[IO](deviceContext)

  def groupCreate(options: GroupCreateOpts): Future[GroupMetaResult] =
    underlying.groupCreate(options).unsafeToFuture

  def groupAddMembers(id: GroupId, users: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupAddMembers(id, users).unsafeToFuture

  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): Future[GroupAccessEditResult] =
    underlying.groupRemoveMembers(id, userRevokes).unsafeToFuture

  def groupGetMetadata(id: GroupId): Future[GroupGetResult] = underlying.groupGetMetadata(id).unsafeToFuture

  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): Future[DocumentEncryptResult] =
    underlying.documentEncrypt(data, options).unsafeToFuture

  def documentDecrypt(encryptedBytes: ByteVector): Future[DocumentDecryptResult] =
    underlying.documentDecrypt(encryptedBytes).unsafeToFuture

  def advanced: IronSdkAdvanced[Future] = IronSdkAdvancedFuture(deviceContext)

  def userCreate(jwt: String, password: String, options: UserCreateOpts): Future[UserCreateResult] =
    underlying.userCreate(jwt, password, options).unsafeToFuture
}
