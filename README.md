# IronCore Labs IronOxide Scala SDK

[![scaladoc](https://javadoc.io/badge2/com.ironcorelabs/ironoxide-scala_2.12/scaladoc.svg)](https://javadoc.io/doc/com.ironcorelabs/ironoxide-scala_2.12)

SDK for using IronCore Labs from Scala server side applications. This library wraps [IronOxide-Java](https://github.com/IronCoreLabs/ironoxide-swig-bindings/tree/main/java)
with more Scala friendly interfaces and types. It presents two top level APIs:

- [`IO`](https://typelevel.org/cats-effect/) based
- [`Future`](https://docs.scala-lang.org/overviews/core/futures.html) based

## Installation

This project is published to [Maven central](https://search.maven.org/artifact/com.ironcorelabs/ironoxide-scala_2.12).

You'll also need to follow the library setup instructions for [IronOxide-Java](https://github.com/IronCoreLabs/ironoxide-swig-bindings/tree/main/java#library) to ensure
you have the proper binary compiled and loaded.

Below you'll find a link of the ironoxide-scala version with which native component you need:

| ironoxide-scala | ironoxide-java                                                                         | Scala Version | Notes
| --------------- | -------------------------------------------------------------------------------------- | ------------- |
| 0.17.0          | [0.15.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.15.0) | 2.13.x
| 0.16.0          | [0.15.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.15.0) | 2.12.x
| 0.15.0          | [0.14.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.14.2) | 2.12.x        | Cats Effect 3.x
| 0.14.0          | [0.14.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.14.2) | 2.12.x

## Usage

The SDK classes can be imported from the `ironoxide.v1` namespace. Other classes can be imported from the
child namespaces `common`, `document`, `group`, `user`, and `beta`.

## Documentation

Further documentation is available on our [docs site](https://ironcorelabs.com/docs).

Copyright (c) 2021 IronCore Labs, Inc. All rights reserved.
