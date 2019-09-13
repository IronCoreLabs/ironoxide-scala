package ironoxide

import scodec.bits.ByteVector
import com.ironcorelabs.scala.sdk._
import org.scalatest.{AsyncWordSpec, Matchers}
import cats.scalatest.EitherValues
import cats.effect.IO

class FullIntegrationTest extends AsyncWordSpec with Matchers with EitherValues {
  try {
    java.lang.System.loadLibrary("ironoxide_java")
  } catch {
    case e: UnsatisfiedLinkError =>
      println("Failed to load ironoxide_java")
      println(
        s"""The value was not found in java.library.path. Path was '${System
             .getProperty("java.library.path")}'.
           |Note that the path should be to the directory where ironoxide_java is, not the actual path. If you build ironoxide_java with
           |`cargo build` then there should be libironoxide_java.* in ../target/debug.""".stripMargin
      );
      //There is no way we can actually continue, so I'm going to do the dirty thing to prevent misleading errors from spewing.
      System.exit(1)
  }

  // Hardcoded user info for these tests because they don't depend on the number of things created for the given user, just
  // the values created.
  val primaryTestUserId = UserId("b29c1ee7-ede9-4401-855a-3a78a34a2759")
  var primaryTestUserSegmentId = 2013L
  var primaryTestUserPrivateDeviceKeyBytes = PrivateKey(
    java.util.Base64.getDecoder.decode("Svt+Z8lfQ8g3FwqeduMyf7X0R1Pbyt9PJXkked7pwuU=")
  )
  var primaryTestUserSigningKeysBytes = DeviceSigningKeyPair(
    java.util.Base64.getDecoder
      .decode("1crhZ4PELDOkzEqX9QbcMQzEDH6dOAr6zybHWryp2pwFhmxRx2EcYD6nUtgVm3OwfaJvGhmIViuj88wV/+duEg==")
  )

  def clearBytes(a: Array[Byte]) =
    for (i <- 0.until(a.length)) {
      a(i) = 0.toByte
    }

  /**
   * Convenience function to create a new DeviceContext instance from the stored off components we need. Takes the
   * users account ID, segment ID, private device key bytes, and signing key bytes and returns a new DeviceContext
   * instance. This helps us prove that we can create this class instance from scratch.
   */
  def createDeviceContext =
    DeviceContext(
      primaryTestUserId,
      primaryTestUserSegmentId,
      primaryTestUserPrivateDeviceKeyBytes,
      primaryTestUserSigningKeysBytes
    )

  "Group Create" should {
    "Create valid group" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val name = GroupName("a name")
      // this is all under the same user now, so this may not be a real test if it already exists, though it won't fail
      val id = GroupId(java.util.UUID.randomUUID.toString)
      val groupCreateResult = sdk.groupCreate(GroupCreateOpts(id, name)).attempt.unsafeRunSync.value

      groupCreateResult.id.id.length shouldBe 36
      groupCreateResult.name.get.name shouldBe name.name
      groupCreateResult.isAdmin shouldBe true
      groupCreateResult.isMember shouldBe true
      groupCreateResult.created should not be null
      groupCreateResult.lastUpdated shouldBe groupCreateResult.created
    }
  }

  "Document encrypt/decrypt" should {
    "succeed for good name and data" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val result = sdk.documentEncrypt(data, DocumentEncryptOpts()).attempt.unsafeRunSync.value

      result.name shouldBe None
      result.id.id.length shouldBe 32
    }

    "roundtrip for single level transform for no name and good data" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(10, 2, 3).map(_.toByte))
      val result = sdk.documentEncrypt(data, DocumentEncryptOpts()).attempt.unsafeRunSync.value

      result.id.id.length shouldBe 32
      result.name.isDefined shouldBe false

      //Now try to decrypt
      val decrypt = sdk.documentDecrypt(result.encryptedData).attempt.unsafeRunSync.value

      decrypt.id.id.length shouldBe 32
      decrypt.name.isDefined shouldBe false
      decrypt.decryptedData shouldBe data
      decrypt.created shouldBe result.created
      decrypt.lastUpdated shouldBe result.lastUpdated
    }

    "grant to specified groups" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))

      val name = GroupName("a name")
      val validGroupUUID = java.util.UUID.randomUUID.toString
      val id = GroupId(validGroupUUID)

      // create a valid group then immediately encrypt to it
      val result = sdk
        .groupCreate(GroupCreateOpts(id, name))
        .flatMap(groupResult => sdk.documentEncrypt(data, DocumentEncryptOpts(Nil, List(groupResult.id))))
        .attempt
        .unsafeRunSync
        .value

      result.changed shouldBe List(primaryTestUserId, id)
    }

    "return failures for bad groups" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val notAUser = UserId("also-definitely-not-a-user")
      val notAGroup = GroupId("definitely-not-generated")
      val result =
        sdk.documentEncrypt(data, DocumentEncryptOpts(List(notAUser), List(notAGroup))).attempt.unsafeRunSync.value

      // what was valid should go through
      result.changed shouldBe List(primaryTestUserId)
      result.errors should contain theSameElementsAs List(
        GroupOrUserAccessError(notAUser, "User could not be found"),
        GroupOrUserAccessError(notAGroup, "Group could not be found")
      )
    }

    "return expected success/failures for policy grant" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val result =
        sdk
          .documentEncrypt(data, DocumentEncryptOpts.withPolicyGrants(true, PolicyGrant(None, None, None, None)))
          .attempt
          .unsafeRunSync
          .value

      result.changed shouldBe List(primaryTestUserId)
      val missingGroupId = s"data_recovery_${primaryTestUserId.id}"
      result.errors shouldBe List(
        GroupOrUserAccessError(
          GroupId(missingGroupId),
          s"Policy refers to unknown user or group ''$missingGroupId' [group]'"
        )
      )
    }
  }

  "Document unmanaged encrypt/decrypt" should {
    "roundtrip through a user" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val encryptResult = sdk.advanced.documentEncryptUnmanaged(data, DocumentEncryptOpts()).attempt.unsafeRunSync.value

      encryptResult.id.id.length shouldBe 32
      encryptResult.encryptedDeks.bytes.isEmpty shouldBe false

      val decrypt =
        sdk.advanced
          .documentDecryptUnmanaged(encryptResult.encryptedData, encryptResult.encryptedDeks)
          .attempt
          .unsafeRunSync
          .value

      decrypt.id.id shouldBe encryptResult.id.id
      decrypt.decryptedData shouldBe data
      decrypt.accessVia.id shouldBe primaryTestUserId.id
      decrypt.accessVia shouldBe a[UserId]

      //Now let's clear byte arrays and verify that they clear in the ByteVector
      clearBytes(decrypt.underlyingBytes)
      decrypt.decryptedData.dropWhile(_ == 0.toByte) shouldBe ByteVector.empty

      clearBytes(encryptResult.encryptedData.underlyingBytes)
      clearBytes(encryptResult.encryptedDeks.underlyingBytes)
      encryptResult.encryptedData.bytes.dropWhile(_ == 0.toByte) shouldBe ByteVector.empty
      encryptResult.encryptedDeks.bytes.dropWhile(_ == 0.toByte) shouldBe ByteVector.empty

    }

    "roundtrip through a group" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))

      val name = GroupName("a name")
      val validGroupUUID = java.util.UUID.randomUUID.toString
      val id = GroupId(validGroupUUID)

      // create a valid group then immediately encrypt to it
      val result = sdk
        .groupCreate(GroupCreateOpts(id, name))
        .flatMap(
          groupResult =>
            sdk.advanced
              .documentEncryptUnmanaged(data, DocumentEncryptOpts.withExplicitGrants(false, Nil, List(groupResult.id)))
        )
        .attempt
        .unsafeRunSync
        .value

      result.changed shouldBe List(id)
      result.encryptedDeks.bytes.isEmpty shouldBe false

      val decrypt =
        sdk.advanced.documentDecryptUnmanaged(result.encryptedData, result.encryptedDeks).attempt.unsafeRunSync.value

      decrypt.id.id shouldBe result.id.id
      decrypt.decryptedData shouldBe data
      decrypt.accessVia.id shouldBe id.id
      decrypt.accessVia shouldBe a[GroupId]
    }

    "return failures for bad groups" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val notAUser = UserId("also-definitely-not-a-user")
      val notAGroup = GroupId("definitely-not-generated")
      val result =
        sdk.advanced
          .documentEncryptUnmanaged(data, DocumentEncryptOpts(List(notAUser), List(notAGroup)))
          .attempt
          .unsafeRunSync
          .value

      result.changed shouldBe List(primaryTestUserId)
      result.encryptedDeks.bytes.isEmpty shouldBe false
      result.errors should contain theSameElementsAs List(
        GroupOrUserAccessError(notAUser, "User could not be found"),
        GroupOrUserAccessError(notAGroup, "Group could not be found")
      )
    }

    "return expected success/failures for policy grant" in {
      val sdk = IronSdkSync[IO](createDeviceContext)
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val result =
        sdk.advanced
          .documentEncryptUnmanaged(
            data,
            DocumentEncryptOpts.withPolicyGrants(true, PolicyGrant(None, None, None, None))
          )
          .attempt
          .unsafeRunSync
          .value

      result.changed shouldBe List(primaryTestUserId)
      val missingGroupId = s"data_recovery_${primaryTestUserId.id}"
      result.errors shouldBe List(
        GroupOrUserAccessError(
          GroupId(missingGroupId),
          s"Policy refers to unknown user or group ''$missingGroupId' [group]'"
        )
      )
      result.encryptedDeks.bytes.isEmpty shouldBe false
    }
  }
}
