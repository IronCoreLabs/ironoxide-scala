package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/** Name of a document. Inner value is validated when calling `toJava`. */
case class DocumentName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentName] =
    syncF.delay(jsdk.DocumentName.validate(name))
}

object DocumentName {
  def apply(name: ju.Optional[jsdk.DocumentName]): Option[DocumentName] =
    name.toScala.map(n => DocumentName(n.getName))
}
