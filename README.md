# IronCore Labs IronOxide Scala SDK

[![scaladoc](https://javadoc-badge.appspot.com/com.ironcorelabs/ironoxide-scala_2.12.svg?label=scaladoc)](https://javadoc-badge.appspot.com/com.ironcorelabs/ironoxide-scala_2.12)

SDK for using IronCore Labs from Scala server side applications. This library wraps [IronOxide-Java](https://github.com/IronCoreLabs/ironoxide-swig-bindings/tree/master/java)
with more Scala friendly interfaces and types. It presents two top level APIs:

- [`IO`](https://typelevel.org/cats-effect/) based
- [`Future`](https://docs.scala-lang.org/overviews/core/futures.html) based

## Installation

This project is published to [Maven central](https://search.maven.org/artifact/com.ironcorelabs/ironoxide-scala_2.12).

You'll also need to follow the library setup instructions for [IronOxide-Java](https://github.com/IronCoreLabs/ironoxide-swig-bindings/tree/master/java#library) to ensure
you have the proper binary compiled and loaded.

Below you'll find a link of the ironoxide-scala version with which native component you need:

| ironoxide-scala | ironoxide-java                                                                         |
| --------------- | -------------------------------------------------------------------------------------- |
| 0.14.0          | [0.14.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.14.2) |
| 0.13.0          | [0.13.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.13.0) |
| 0.12.1          | [0.12.1](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.12.1) |
| 0.12.0          | [0.12.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.12.0) |
| 0.11.0          | [0.11.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.11.0) |
| 0.10.x          | [0.11.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.11.0) |
| 0.9.0           | [0.11.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.11.0) |
| 0.8.0           | [0.9.0](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.8.0)   |
| 0.7.x           | [0.8.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.8.2)   |
| 0.6.0           | [0.7.2](https://github.com/IronCoreLabs/ironoxide-swig-bindings/releases/tag/v0.7.2)   |

## Usage

The SDK classes can be imported from the `ironoxide.v1` namespace. Other classes can be imported from the
child namespaces `common`, `document`, `group`, `user`, and `beta`.

## Documentation

Further documentation is available on our [docs site](https://ironcorelabs.com/docs).

Copyright (c) 2019 IronCore Labs, Inc. All rights reserved.
