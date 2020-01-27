package ironoxide.v1.document

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}

/** ID of a document. Unique within the segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`. */
case class DocumentId(id: String) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentId] =
    syncF.delay(jsdk.DocumentId.validate(id))
}

object DocumentId {
  def apply(id: jsdk.DocumentId): DocumentId = DocumentId(id.getId)
}
