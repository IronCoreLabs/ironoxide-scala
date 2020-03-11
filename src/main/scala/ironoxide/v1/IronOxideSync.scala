package ironoxide.v1

import cats.effect.Sync
import cats.implicits._
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.beta._
import ironoxide.v1.common._
import ironoxide.v1.document._
import ironoxide.v1.group._
import ironoxide.v1.user._
import scala.concurrent.duration.Duration
import scodec.bits.ByteVector

case class IronOxideSync[F[_]](underlying: jsdk.IronOxide)(implicit syncF: Sync[F]) extends IronOxide[F] {

  def clearPolicyCache: F[Long] =
    syncF.delay(underlying.clearPolicyCache)

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

  def groupList: F[List[GroupMetaResult]] =
    syncF.delay(underlying.groupList.getResult.toList.map(GroupMetaResult(_)))

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
    syncF.delay(DocumentDecryptResult(underlying.documentDecrypt(encryptedBytes.toArray)))

  def documentList: F[List[DocumentListMeta]] =
    syncF.delay(underlying.documentList.getResult.toList.map(DocumentListMeta(_)))

  def documentGetMetadata(id: DocumentId): F[DocumentMetadataResult] =
    for {
      javaId <- id.toJava
      result <- syncF.delay(underlying.documentGetMetadata(javaId))
    } yield DocumentMetadataResult(result)

  def documentGetIdFromBytes(encryptedDocument: ByteVector): F[DocumentId] =
    syncF.delay(DocumentId(underlying.documentGetIdFromBytes(encryptedDocument.toArray)))

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

  def userCreate(
    jwt: String,
    password: String,
    options: UserCreateOpts,
    timeout: Option[Duration]
  ): F[UserCreateResult] =
    IronOxide.userCreate(jwt, password, options, timeout)

  def userVerify(jwt: String, timeout: Option[Duration]): F[Option[UserResult]] =
    IronOxide.userVerify(jwt, timeout)

  def userGetPublicKey(users: List[UserId]): F[List[UserWithKey]] =
    for {
      javaUsers <- users.traverse(_.toJava).map(_.toArray)
      result    <- syncF.delay(underlying.userGetPublicKey(javaUsers).toList)
    } yield result.map(UserWithKey(_))

  def userListDevices: F[List[UserDevice]] =
    syncF.delay(underlying.userListDevices.getResult.toList.map(UserDevice(_)))

  def userDeleteDevice(deviceId: Option[DeviceId]): F[DeviceId] =
    for {
      javaId <- deviceId.traverse(_.toJava)
      result <- syncF.delay(underlying.userDeleteDevice(javaId.orNull))
    } yield DeviceId(result)

  def userRotatePrivateKey(password: String): F[UserUpdatePrivateKeyResult] =
    syncF.delay(UserUpdatePrivateKeyResult(underlying.userRotatePrivateKey(password)))

  def createBlindIndex(groupId: GroupId): F[EncryptedBlindIndexSalt] =
    for {
      javaId <- groupId.toJava
      result <- syncF.delay(underlying.createBlindIndex(javaId))
    } yield EncryptedBlindIndexSalt(result, underlying)

  def advanced: IronOxideAdvanced[F] = IronOxideAdvancedSync(underlying)
}
