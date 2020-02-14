package ironoxide.v1

import cats.effect.IO
import ironoxide.v1.common._
import ironoxide.v1.document._
import ironoxide.v1.group._
import ironoxide.v1.user._
import scala.concurrent.duration.Duration
import scala.concurrent.Future
import scala.util.Try
import scodec.bits.ByteVector

case class IronOxideFuture(underlying: IronOxide[IO]) extends IronOxide[Future] {

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

  def advanced: IronOxideAdvanced[Future] = IronOxideAdvancedFuture(underlying.advanced)

  def userCreate(
    jwt: String,
    password: String,
    options: UserCreateOpts,
    timeout: Option[Duration]
  ): Future[UserCreateResult] =
    underlying.userCreate(jwt, password, options, timeout).unsafeToFuture

  def userVerify(jwt: String, timeout: Option[Duration]): Future[Option[UserResult]] =
    underlying.userVerify(jwt, timeout).unsafeToFuture

  def userGetPublicKey(users: List[UserId]): Future[List[UserWithKey]] =
    underlying.userGetPublicKey(users).unsafeToFuture

  def userListDevices: Future[List[UserDevice]] =
    underlying.userListDevices.unsafeToFuture

  def userDeleteDevice(deviceId: Option[DeviceId]): Future[DeviceId] =
    underlying.userDeleteDevice(deviceId).unsafeToFuture

  def userRotatePrivateKey(password: String): Future[UserUpdatePrivateKeyResult] =
    underlying.userRotatePrivateKey(password).unsafeToFuture
}

object IronOxideFuture {
  def tryInitialize(deviceContext: DeviceContext, config: IronOxideConfig): Try[IronOxideFuture] =
    IronOxide.tryInitialize[IO](deviceContext, config).map(IronOxideFuture(_))

  def initialize(deviceContext: DeviceContext, config: IronOxideConfig): Future[IronOxide[Future]] =
    IronOxide.initialize[IO](deviceContext, config).map(IronOxideFuture(_)).unsafeToFuture

  def initializeAndRotate(
    deviceContext: DeviceContext,
    password: String,
    config: IronOxideConfig,
    timeout: Option[Duration]
  ): Future[IronOxide[Future]] =
    IronOxide.initializeAndRotate[IO](deviceContext, password, config, timeout).map(IronOxideFuture(_)).unsafeToFuture

  def userCreate[F[_]](
    jwt: String,
    password: String,
    options: UserCreateOpts,
    timeout: Option[Duration]
  ): Future[UserCreateResult] =
    IronOxide.userCreate[IO](jwt, password, options, timeout).unsafeToFuture
}
