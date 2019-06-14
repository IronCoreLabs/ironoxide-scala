package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/**
  * ID of a document. Unique within the segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`.
  */
case class DocumentId(id: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentId] = syncF.delay(jsdk.DocumentId.validate(id))
}

/**
  * Document name type. Validates that the provided document name isn't an empty string
  */
case class DocumentName(name: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentName] =
    syncF.delay(jsdk.DocumentName.validate(name))
}

object DocumentName {
  private[sdk] def fromJava(name: ju.Optional[jsdk.DocumentName]): Option[DocumentName] =
    name.toScala.map(d => DocumentName(d.getName))

}
