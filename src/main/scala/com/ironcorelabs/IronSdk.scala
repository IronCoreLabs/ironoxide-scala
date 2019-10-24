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
   * Create a new user within the IronCore system.
   *
   * @param jwt valid IronCore or Auth0 JWT
   * @param password password used to encrypt and escrow the user's private master key
   * @param options user creation options. Use `new UserCreateOpts()` for defaults
   * @return Newly generated [[UserCreateResult]]. For most use cases, the public key can be discarded as IronCore escrows your user's keys. The escrowed keys are unlocked by the provided password.
   */
  def userCreate(jwt: String, password: String, options: UserCreateOpts): F[UserCreateResult]

  //TODO: docs
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
