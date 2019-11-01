package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector

/**
 * Ability to make authenticated requests to the IronCore API. Instantiated with the details
 * of an account's various IDs, devices, and signing keys. Once instantiated, all operations will be
 * performed in the context of the account provided.
 */
trait IronSdk[F[_]] {

  /**
   * Encrypts the provided document bytes.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncrypt(data: ByteVector, options: DocumentEncryptOpts): F[DocumentEncryptResult]

  /**
   * Encrypts the provided document bytes.
   *
   * @param data bytes of the document to encrypt
   * @param options optional document encrypt parameters
   */
  def documentEncrypt(data: Array[Byte], options: DocumentEncryptOpts): F[DocumentEncryptResult] =
    documentEncrypt(ByteVector.view(data), options)

  /**
   * Decrypts the provided encrypted document and returns details about the document as well as its decrypted bytes.
   *
   * @param encryptedBytes bytes of encrypted document. Should be the same bytes returned from documentEncrypt
   * @return a [[DocumentDecryptResult]] with metadata about the provided document as well as the decrypted document bytes
   */
  def documentDecrypt(encryptedBytes: ByteVector): F[DocumentDecryptResult]

  /**
   * Decrypts the provided encrypted document and returns details about the document as well as its decrypted bytes.
   *
   * @param encryptedBytes bytes of encrypted document. Should be the same bytes returned from documentEncrypt
   * @return a [[DocumentDecryptResult]] with metadata about the provided document as well as the decrypted document bytes
   */
  def documentDecrypt(encryptedBytes: Array[Byte]): F[DocumentDecryptResult] =
    documentDecrypt(ByteVector.view(encryptedBytes))

  /**
   * Creates a group. The creating user will become a group admin.
   *
   * @param options group creation options. Use `new GroupCreateOpts()` for defaults
   */
  def groupCreate(options: GroupCreateOpts): F[GroupMetaResult]

  /**
   * Add a list of users as members of a group.
   *
   * @param id      id of the group to add members to
   * @param users   the list of users thet will be added to the group as members
   * @return all the users that were added and all the users that were not added with the reason they were not
   */
  def groupAddMembers(id: GroupId, users: List[UserId]): F[GroupAccessEditResult]

  /**
   * Remove a list of users as members from the group.
   *
   * @param id          id of the group to remove members from
   * @param userRevokes list of user ids to remove as members
   * @return list of users that were removed and the users that failed to be removed with the reason they were not
   */
  def groupRemoveMembers(id: GroupId, userRevokes: List[UserId]): F[GroupAccessEditResult]

  /**
   * Add a list of users as admins of a group.
   *
   * @param id      id of the group to add admins to
   * @param users   the list of users that will be added to the group as admins
   * @return all the users that were added and all the users that were not added with the reason they were not
   */
  def groupAddAdmins(id: GroupId, users: List[UserId]): F[GroupAccessEditResult]

  /**
   * Remove a list of users as admins from the group.
   *
   * @param id          id of the group to remove admins from
   * @param userRevokes list of user ids to remove as admins
   * @return list of users that were removed and the users that failed to be removed with the reason they were not
   */
  def groupRemoveAdmins(id: GroupId, userRevokes: List[UserId]): F[GroupAccessEditResult]

  /**
   * Gets the full metadata for a specific group given its ID.
   *
   * @param id unique id of the group to retrieve
   * @return details about the requested group
   */
  def groupGetMetadata(id: GroupId): F[GroupGetResult]

  /**
   * Creates a new user within the IronCore system.
   *
   * @param jwt valid IronCore or Auth0 JWT
   * @param password password used to encrypt and escrow the user's private master key
   * @param options user creation options. Use `new UserCreateOpts()` for defaults
   * @return Newly generated [[UserCreateResult]]. For most use cases, the public key can be discarded as IronCore escrows your user's keys. The escrowed keys are unlocked by the provided password.
   */
  def userCreate(jwt: String, password: String, options: UserCreateOpts): F[UserCreateResult]

  /**
   * Rotate the current user's private key, but leave the public key the same.
   * There's no black magic here! This is accomplished via multi-party computation with the
   * IronCore webservice.
   *
   * @param password password to unlock the current user's master private key
   *
   * @return The (encrypted) updated private key and associated metadata
   */
  def userRotatePrivateKey(password: String): F[UserUpdatePrivateKeyResult]

  /**
   * Accesses advanced SDK operations.
   *
   * @return an instance of the [[IronSdkAdvanced]]
   */
  def advanced: IronSdkAdvanced[F]
}

object IronSdk {
  import cats.effect.Sync
  import cats.implicits._

  /**
   * Initialize IronSdk with a device. Verifies that the provided user/segment exists and the provided device
   * keys are valid and exist for the provided account.
   *
   * @param deviceContext device context used to initialize the IronSdk with a set of device keys
   * @return an instance of the IronSdk
   */
  def initialize[F[_]](deviceContext: DeviceContext)(implicit syncF: Sync[F]): F[IronSdk[F]] =
    deviceContext.toJava.map(com.ironcorelabs.sdk.IronSdk.initialize).map(IronSdkSync(_))

  /**
   * Initialize IronSdk with a device. Verifies that the provided user/segment exists and the provided device
   * keys are valid and exist for the provided account.
   * After initialization, checks whether the calling user's private key needs rotation and rotates it
   * if necessary.
   *
   * @param deviceContext device context used to initialize the IronSdk with a set of device keys
   * @param password password used to encrypt and escrow the user's private master key
   * @return an instance of the IronSdk
   */
  def initializeAndRotate[F[_]](deviceContext: DeviceContext, password: String)(
    implicit syncF: Sync[F]
  ): F[IronSdk[F]] =
    deviceContext.toJava.map(com.ironcorelabs.sdk.IronSdk.initializeAndRotate(_, password)).map(IronSdkSync(_))

  /**
   * Create a new user within the IronCore system.
   *
   * @param jwt Valid IronCore or Auth0 JWT
   * @param password Password used to encrypt and escrow the user's private master key
   * @param options user creation options. Use `new UserCreateOpts()` for defaults
   * @return Newly generated [[UserCreateResult]]. For most use cases, the public key can be discarded as IronCore escrows your user's keys. The escrowed keys are unlocked by the provided password.
   */
  def userCreate[F[_]](jwt: String, password: String, options: UserCreateOpts)(
    implicit syncF: Sync[F]
  ): F[UserCreateResult] =
    options.toJava.map { javaOpts =>
      UserCreateResult(com.ironcorelabs.sdk.IronSdk.userCreate(jwt, password, javaOpts))
    }
}
