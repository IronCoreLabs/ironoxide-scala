package com.ironcorelabs.scala.sdk

import scodec.bits.ByteVector
import org.scalatest.{AsyncWordSpec, Matchers, OptionValues}
import cats.scalatest.EitherValues
import cats.effect.IO
import com.ironcorelabs.{sdk => jsdk}

class FullIntegrationTest extends AsyncWordSpec with Matchers with EitherValues with OptionValues {
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
  val primaryTestUserSegmentId = 2013L
  val primaryTestUserDevicePrivateKeyBytes = PrivateKey(
    java.util.Base64.getDecoder.decode("Svt+Z8lfQ8g3FwqeduMyf7X0R1Pbyt9PJXkked7pwuU=")
  )
  val primaryTestUserSigningPrivateKeyBytes = DeviceSigningPrivateKey(
    java.util.Base64.getDecoder
      .decode("1crhZ4PELDOkzEqX9QbcMQzEDH6dOAr6zybHWryp2pwFhmxRx2EcYD6nUtgVm3OwfaJvGhmIViuj88wV/+duEg==")
  )
  val validDeviceId = DeviceId(1)
  val validGroupId = GroupId(java.util.UUID.randomUUID.toString)

  def clearBytes(a: Array[Byte]) =
    for (i <- 0.until(a.length)) {
      a(i) = 0.toByte
    }

  val deviceContext =
    DeviceContext(
      validDeviceId,
      primaryTestUserId,
      primaryTestUserSegmentId,
      primaryTestUserDevicePrivateKeyBytes,
      primaryTestUserSigningPrivateKeyBytes
    )

  val sdk = deviceContext.toJava[IO].map(d => IronSdkSync[IO](jsdk.IronSdk.initialize(d))).unsafeRunSync

  "User Create" should {
    "fail for invalid jwt" in {
      val jwt =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NTA3NzE4MjMsImlhdCI6MTU1MDc3MTcwMywia2lkIjo1NTEsInBpZCI6MTAxMiwic2lkIjoidGVzdC1zZWdtZW50Iiwic3ViIjoiYTAzYjhlNTYtMTVkMi00Y2Y3LTk0MWYtYzYwMWU1NzUxNjNiIn0.vlqt0da5ltA2dYEK9i_pfRxPd3K2uexnkbAbzmbjW65XNcWlBOIbcdmmQLnSIZkRyTORD3DLXOIPYbGlApaTCR5WbaR3oPiSsR9IqdhgMEZxCcarqGg7b_zzwTP98fDcALGZNGsJL1hIrl3EEXdPoYjsOJ5LMF1H57NZiteBDAsm1zfXgOgCtvCdt7PQFSCpM5GyE3und9VnEgjtcQ6HAZYdutqjI79vaTnjt2A1X38pbHcnfvSanzJoeU3szwtBiVlB3cfXbROvBC7Kz8KvbWJzImJcJiRT-KyI4kk3l8wAs2FUjSRco8AQ1nIX21QHlRI0vVr_vdOd_pTXOUU51g"
      val resp = IronSdk.userCreate[IO](jwt, "foo", UserCreateOpts(true)).attempt.unsafeRunSync
      resp shouldBe 'left
    }
  }

  "User Rotate Private Key" should {
    "fail for incorrect password" in {
      val resp = sdk.userRotatePrivateKey("this isn't my password").attempt.unsafeRunSync
      resp shouldBe 'left
    }
  }

  "Group Create" should {
    "Create valid group" in {
      val name = GroupName("a name")
      val groupCreateResult = sdk.groupCreate(GroupCreateOpts(validGroupId, name)).attempt.unsafeRunSync.value

      groupCreateResult.id.id shouldBe validGroupId.id
      groupCreateResult.name.get.name shouldBe name.name
      groupCreateResult.isAdmin shouldBe true
      groupCreateResult.isMember shouldBe true
      groupCreateResult.created should not be null
      groupCreateResult.lastUpdated shouldBe groupCreateResult.created
      groupCreateResult.needsRotation.value shouldBe false
    }
  }

  "Group Add Members" should {
    "Fail for nonexistent GroupId" in {
      val maybeAddMembersResult = sdk.groupAddMembers(GroupId("kumquat"), Nil).attempt.unsafeRunSync
      maybeAddMembersResult.isLeft shouldBe true
    }
    "Succeed in the call, but fail to add an existing member" in {
      val addMembersResult = sdk.groupAddMembers(validGroupId, List(primaryTestUserId)).attempt.unsafeRunSync.value
      addMembersResult.failed.length shouldBe 1
      addMembersResult.failed.head.error should include("User was already a member")
    }
    "Fail for nonexistent UserId" in {
      val maybeAddMembersResult = sdk.groupAddMembers(validGroupId, List(UserId("spanakopita"))).attempt.unsafeRunSync
      maybeAddMembersResult.isLeft shouldBe true
    }
  }

  "Group Remove Members" should {
    "fail for nonexistent GroupId" in {
      val maybeRemoveMembersResult = sdk.groupRemoveMembers(GroupId("icl"), Nil).attempt.unsafeRunSync
      maybeRemoveMembersResult.isLeft shouldBe true
    }
    "Succeed in the call, but fail to remove a nonexistent UserId" in {
      val removeMembersResult =
        sdk.groupRemoveMembers(validGroupId, List(UserId("tony"))).attempt.unsafeRunSync.value
      removeMembersResult.failed.length shouldBe 1
      removeMembersResult.failed.head.error should include("could not be removed")
    }
    "succeed for valid GroupId/UserId" in {
      val removeMembersResult =
        sdk.groupRemoveMembers(validGroupId, List(primaryTestUserId)).attempt.unsafeRunSync.value
      removeMembersResult.succeeded.length shouldBe 1
      removeMembersResult.succeeded.head shouldBe primaryTestUserId
    }
  }

  "Group Add Members again" should {
    "succeed for valid UserId" in {
      val addMembersResult = sdk.groupAddMembers(validGroupId, List(primaryTestUserId)).attempt.unsafeRunSync.value
      addMembersResult.succeeded.length shouldBe 1
      addMembersResult.succeeded.head shouldBe primaryTestUserId
    }
  }

  "Group Add Admins" should {
    "Fail for nonexistent GroupId" in {
      val maybeAddAdminsResult = sdk.groupAddAdmins(GroupId("tony"), Nil).attempt.unsafeRunSync
      maybeAddAdminsResult.isLeft shouldBe true
    }
    "Succeed in the call, but fail to add an existing member" in {
      val addAdminsResult = sdk.groupAddAdmins(validGroupId, List(primaryTestUserId)).attempt.unsafeRunSync.value
      addAdminsResult.failed.length shouldBe 1
      addAdminsResult.failed.head.error should include("User was already an admin")
    }
    "Fail for nonexistent UserId" in {
      val maybeAddAdminsResult = sdk.groupAddAdmins(validGroupId, List(UserId("steve"))).attempt.unsafeRunSync
      maybeAddAdminsResult.isLeft shouldBe true
    }
  }

  "Group Remove Admins" should {
    "fail for nonexistent GroupId" in {
      val maybeRemoveAdminsResult = sdk.groupRemoveAdmins(GroupId("notarealgroup"), Nil).attempt.unsafeRunSync
      maybeRemoveAdminsResult.isLeft shouldBe true
    }
    "Succeed in the call, but fail to remove a nonexistent UserId" in {
      val removeAdminsResult =
        sdk.groupRemoveAdmins(validGroupId, List(UserId("tony"))).attempt.unsafeRunSync.value
      removeAdminsResult.failed.length shouldBe 1
      removeAdminsResult.failed.head.error should include("could not be removed")
    }
    "fail to remove sole group admin" in {
      val removeAdminsResult =
        sdk.groupRemoveAdmins(validGroupId, List(primaryTestUserId)).attempt.unsafeRunSync.value
      removeAdminsResult.failed.length shouldBe 1
      removeAdminsResult.failed.head.error should include("could not be removed")
    }
  }

  "Group Get" should {
    "Return data for valid group admin" in {
      val groupGetResult = sdk.groupGetMetadata(validGroupId).attempt.unsafeRunSync.value

      groupGetResult.id.id shouldBe validGroupId.id
      groupGetResult.name.get.name shouldBe "a name"
      groupGetResult.isAdmin shouldBe true
      groupGetResult.isMember shouldBe true
      groupGetResult.adminList.value shouldBe List(primaryTestUserId)
      groupGetResult.memberList.value shouldBe List(primaryTestUserId)
      groupGetResult.created should not be null
      groupGetResult.lastUpdated should be > groupGetResult.created
      groupGetResult.needsRotation.value shouldBe false
    }
    "Fail for invalid group id" in {
      val maybeGroupGetResult = sdk.groupGetMetadata(GroupId("tsp")).attempt.unsafeRunSync
      maybeGroupGetResult.isLeft shouldBe true
    }
  }

  "DeviceContext" should {
    "succeed roundtrip serialize/deserialize" in {
      val jsonString = deviceContext.toJsonString[IO].unsafeRunSync
      val deserialized = DeviceContext.fromJsonString[IO](jsonString).unsafeRunSync
      val deviceId = deviceContext.deviceId.id
      val accountId = deviceContext.userId.id
      val segmentId = deviceContext.segmentId
      val signingPrivateKeyBase64 = deviceContext.signingPrivateKey.bytes.toBase64
      val devicePrivateKeyBase64 = deviceContext.devicePrivateKey.bytes.toBase64
      val expectJson =
        s"""{"deviceId":$deviceId,"accountId":"$accountId","segmentId":$segmentId,"signingPrivateKey":"$signingPrivateKeyBase64","devicePrivateKey":"$devicePrivateKeyBase64"}"""

      jsonString shouldBe expectJson
      deviceContext.deviceId.id shouldBe deserialized.deviceId.id
      deviceContext.devicePrivateKey.bytes shouldBe deserialized.devicePrivateKey.bytes
      deviceContext.segmentId shouldBe deserialized.segmentId
      deviceContext.signingPrivateKey.bytes shouldBe deserialized.signingPrivateKey.bytes
      deviceContext.userId.id shouldBe deserialized.userId.id
    }
    "fail for invalid json" in {
      val resp = DeviceContext.fromJsonString[IO]("aaa").attempt.unsafeRunSync
      resp shouldBe 'left
    }
  }

  "Document encrypt/decrypt" should {
    "succeed for good name and data" in {
      val data = ByteVector(List(1, 2, 3).map(_.toByte))
      val result = sdk.documentEncrypt(data, DocumentEncryptOpts()).attempt.unsafeRunSync.value

      result.name shouldBe None
      result.id.id.length shouldBe 32
    }

    "roundtrip for single level transform for no name and good data" in {
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
