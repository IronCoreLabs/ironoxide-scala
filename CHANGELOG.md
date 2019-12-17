# Changelog

## 0.7.2

- Add `groupRotatePrivateKey()` to rotate a group's private key.
- Change `IronSdk.initializeAndRotate()` to also rotate the private keys of all necessary groups that the calling user is an admin of.

## 0.7.1

- Make compatible with Java 8

## 0.7.0

- Update to ironoxide-java 0.8.0
- Add `needsRotation` to `GroupCreateOpts` to specify whether a group needs its private key rotated on creation.
- Add `addAsAdmin`, `owner`, `admins`, and `members` to `GroupCreateOpts` to allow adding users to a group on creation.

## 0.6.0

- Add `IronSdk.initialize()` to initialize IronSdk with a device.
- Add `IronSdk.initializeAndRotate()` to initialize IronSdk with a device, then rotate the user's private key if needed.
- Add `userRotatePrivateKey()` to rotate the user's private key.
- Add `groupAddAdmins()` and `groupRemoveAdmins()` to add/remove group admins.
- Add `generateNewDevice()` to generate a new device for the user.

## 0.5.3

- Update to ironoxide-java 0.7.1
- Add `groupAddMembers()` and `groupRemoveMembers()` to add/remove group members.

## 0.5.2

- Add `groupGetMetadata()` to return group data for a given `GroupId`
- Add `needsRotation` as an `Option[Boolean]` to `GroupMetaResult`

## 0.5.1

- Update to ironoxide-java 0.6.1

## 0.5.0

- Add `userCreate()` to create a new user from a jwt, password, and user creation options.
- Change parameters of `DeviceContext`, adding functions `toJsonString()` and `fromJsonString()`
- Rename `DeviceSigningKeyPair` to `DeviceSigningPrivateKey`

## 0.4.0

- Cats effect 2.0.0
- Scala 2.12.9
- Update `changed` and `errors` on `DocumentEncryptUnmanagedResult` to be scala friendly types.
- Update `changed` and `errors` on `DocumentEncryptResult` to be scala friendly types.
- Add helper methods that take `Array[Byte]` instead of `ByteVector` to keep people from not sharing underlying bytes.

## 0.3.4

- Documentation overhaul
- Fix bug around sharing of underlying `Array[Byte]`

## 0.3.3

- Allow advanced encrypt/decrypt with unmanaged edeks.

## 0.3.1

- Consume latest ironoxide which allows safe concurrent access for all APIs.

## 0.3.0

- add ability to grant access via policy.

## 0.2.0

- add `grantToAuthor` boolean option to `DocumentEncryptOpts` to allow the caller to decide whether the document is
  encrypted to them or not.

## 0.1.1

- initial open source release
