package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import com.ironcorelabs.{sdk => jsdk}

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
   * List all of the documents that the current user is able to decrypt.
   *
   * @return a List of [[DocumentListMeta]] with metadata about each document the user can decrypt.
   */
  def documentList(): F[List[DocumentListMeta]]

  /**
   * Get the metadata for a specific document given its id.
   *
   * @param id unique id of the document to retrieve
   * @return [[DocumentMetadataResult]] with details about the requested document.
   */
  def documentGetMetadata(id: DocumentId): F[DocumentMetadataResult]

  /**
   * Attempt to parse the document id out of an encrypted document.
   *
   * @param encryptedDocument encrypted document bytes
   * @return extracted id
   */
  def documentGetIdFromBytes(encryptedDocument: ByteVector): F[DocumentId]

  /**
   * Attempt to parse the document id out of an encrypted document.
   *
   * @param encryptedDocument encrypted document bytes
   * @return extracted id
   */
  def documentGetIdFromBytes(encryptedDocument: Array[Byte]): F[DocumentId] =
    documentGetIdFromBytes(ByteVector.view(encryptedDocument))

  /**
   * Update a document name to a new value or clear its value.
   *
   * @param id      id of the document to update
   * @param name    new name for the document. Provide a Some([[DocumentName]]) to update to a new name or None to clear the name field.
   * @return metadata about the document that was updated.
   */
  def documentUpdateName(id: DocumentId, name: Option[DocumentName]): F[DocumentMetadataResult]

  /**
   * Update the encrypted content of an existing document. Persists any existing access to other users and groups.
   *
   * @param id               id of document to update
   * @param newDocumentData  updated document content to encrypt
   */
  def documentUpdateBytes(id: DocumentId, newDocumentData: ByteVector): F[DocumentEncryptResult]

  /**
   * Update the encrypted content of an existing document. Persists any existing access to other users and groups.
   *
   * @param id               id of document to update
   * @param newDocumentData  updated document content to encrypt
   */
  def documentUpdateBytes(id: DocumentId, newDocumentData: Array[Byte]): F[DocumentEncryptResult] =
    documentUpdateBytes(id, ByteVector.view(newDocumentData))

  /**
   * Grant access to a document. Recipients of document access can be either users or groups.
   *
   * @param documentId id of the document whose access is is being modified
   * @param userGrants list of user grants
   * @param groupGrants list of group grants
   * @return each individual grant to a user/group succeeded or failed
   */
  def documentGrantAccess(
    documentId: DocumentId,
    userGrants: List[UserId],
    groupGrants: List[GroupId]
  ): F[DocumentAccessResult]

  /**
   * Revoke access from a document. Revocation of document access can be either users or groups.
   *
   * @param documentId     id of the document whose access is is being modified
   * @param userRevokes    list of user revokes
   * @param groupRevokes   list of group revokes
   * @return each individual revoke from a user/group either succeeded or failed
   */
  def documentRevokeAccess(
    documentId: DocumentId,
    userRevokes: List[UserId],
    groupRevokes: List[GroupId]
  ): F[DocumentAccessResult]

  /**
   * Creates a group
   *
   * @param options group creation options. Use `GroupCreateOpts$.apply()` for defaults
   */
  def groupCreate(options: GroupCreateOpts): F[GroupCreateResult]

  /**
   * Update a group name to a new value or clear its value.
   *
   * @param id      id of the group to update
   * @param name    new name for the group. Provide a Some([[GroupName]]) to update to a new name or None to clear the name field
   * @return metadata about the group that was updated
   */
  def groupUpdateName(id: GroupId, name: Option[GroupName]): F[GroupMetaResult]

  /**
   * List all of the groups that the current user is either an admin or member of.
   *
   * @return list of (abbreviated) metadata about each group the user is a part of
   */
  def groupList(): F[List[GroupMetaResult]]

  /**
   * Delete the identified group.
   *
   * @param id unique id of group
   * @return the deleted group id
   */
  def groupDelete(id: GroupId): F[GroupId]

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
   * Rotate the provided group's private key, but leave the public key the same.
   * There's no black magic here! This is accomplished via multi-party computation with the
   * IronCore webservice.
   * Note: You must be an admin of the group in order to rotate its private key.
   *
   * @param id id of the group you wish to rotate the private key of
   * @return id of the group and associated metadata
   */
  def groupRotatePrivateKey(id: GroupId): F[GroupUpdatePrivateKeyResult]

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
   * Verify a user given a JWT for their user record.
   *
   * @param jwt valid IronCore JWT
   * @return Option of whether the user's account record exists in the IronCore system or not
   */
  def userVerify(jwt: String): F[Option[UserResult]]

  /**
   * Get a list of user public keys given their IDs. Allows discovery of which user IDs have keys in the
   * IronCore system to determine if they can be added to groups or have documents shared with them.
   *
   * @param users list of user IDs to check
   * @return List of users and their public keys. Only users who have public keys will be returned in the map
   */
  def userGetPublicKey(users: List[UserId]): F[List[UserWithKey]]

  /**
   * Get all the devices for the current user
   *
   * @return all devices for the current user, sorted by the device id
   */
  def userListDevices(): F[List[UserDevice]]

  /**
   * Delete a user device.
   *
   * If deleting the currently signed in device (None for `deviceId`), the sdk will need to be
   * reinitialized with `IronSdk.initialize()` before further use.
   *
   * @param deviceId id of the device to delete. If None, delete the currently signed in device. Use [[userListDevices]] to get ids.
   * @return id of deleted device
   */
  def userDeleteDevice(deviceId: Option[DeviceId]): F[DeviceId]

  /**
   * Rotate the current user's private key, but leave the public key the same.
   * There's no black magic here! This is accomplished via multi-party computation with the
   * IronCore webservice.
   *
   * @param password password to unlock the current user's master private key
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
    deviceContext.toJava.map(jsdk.IronSdk.initialize).map(IronSdkSync(_))

  /**
   * Initialize IronSdk with a device. Verifies that the provided user/segment exists and the provided device
   * keys are valid and exist for the provided account.
   * After initialization, checks whether the calling user's private key needs rotation and rotates it
   * if necessary, then does the same for each group the user is an admin of.
   *
   * @param deviceContext device context used to initialize the IronSdk with a set of device keys
   * @param password password used to encrypt and escrow the user's private master key
   * @return an instance of the IronSdk
   */
  def initializeAndRotate[F[_]](deviceContext: DeviceContext, password: String)(
    implicit syncF: Sync[F]
  ): F[IronSdk[F]] =
    deviceContext.toJava.map(jsdk.IronSdk.initializeAndRotate(_, password)).map(IronSdkSync(_))

  /**
   * Generate a new device for the user specified in the signed JWT.
   * This will result in a new transform key (from the user's master private key to the new device's public key)
   * being generated and stored with the IronCore Service.
   *
   * @param jwt                 valid IronCore JWT
   * @param password            password used to encrypt and escrow the user's private key
   * @param deviceCreateOptions optional values, like device name
   * @return details about the newly created device
   */
  def generateNewDevice[F[_]](jwt: String, password: String, deviceCreateOptions: DeviceCreateOpts)(
    implicit syncF: Sync[F]
  ): F[DeviceContext] =
    deviceCreateOptions.toJava.map { javaOpts =>
      DeviceContext(jsdk.IronSdk.generateNewDevice(jwt, password, javaOpts))
    }

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
      UserCreateResult(jsdk.IronSdk.userCreate(jwt, password, javaOpts))
    }

  /**
   * Verify a user given a JWT for their user record.
   *
   * @param jwt valid IronCore JWT
   * @return option of whether the user's account record exists in the IronCore system or not
   */
  def userVerify[F[_]](jwt: String)(implicit syncF: Sync[F]): F[Option[UserResult]] =
    syncF.delay(UserResult(jsdk.IronSdk.userVerify(jwt)))
}
