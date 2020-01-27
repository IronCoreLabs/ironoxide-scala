package ironoxide.v1.document

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.OptionalOps
import java.{util => ju}

/** Name of a document. Inner value is validated when calling `toJava`. */
case class DocumentName(name: String) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DocumentName] =
    syncF.delay(jsdk.DocumentName.validate(name))
}

object DocumentName {
  def apply(name: ju.Optional[jsdk.DocumentName]): Option[DocumentName] =
    name.toScala.map(n => DocumentName(n.getName))
}
