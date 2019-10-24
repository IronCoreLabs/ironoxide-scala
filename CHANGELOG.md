# Changelog

## 0.6.1
- Update to ironoxide-java 0.6.1

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

Allow advanced encrypt/decrypt with unmanaged edeks.

## 0.3.1

Consume latest ironoxide which allows safe concurrent access for all APIs.

## 0.3.0

- add ability to grant access via policy.

## 0.2.0

- add `grantToAuthor` boolean option to `DocumentEncryptOpts` to allow the caller to decide whether the document is
  encrypted to them or not.

## 0.1.1

- initial open source release
