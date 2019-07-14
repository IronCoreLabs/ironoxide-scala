package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import cats.implicits._

/**
 * ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`
 */
case class PolicyGrant(
  category: Option[Category],
  sensitivity: Option[Sensitivity],
  dataSubject: Option[DataSubject],
  substituteId: Option[UserId]
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.PolicyGrant] =
    for {
      javaCat          <- category.traverse(_.toJava).map(_.orNull)
      javaSens         <- sensitivity.traverse(_.toJava).map(_.orNull)
      javaDataSub      <- dataSubject.traverse(_.toJava).map(_.orNull)
      javaSubstituteId <- substituteId.traverse(_.toJava).map(_.orNull)
    } yield new jsdk.PolicyGrant(javaCat, javaSens, javaDataSub, javaSubstituteId)
}

/**
 * ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`
 */
case class Sensitivity(id: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Sensitivity] =
    syncF.delay(jsdk.Sensitivity.validate(id))
}

object Sensitivity {
  def apply(s: jsdk.Sensitivity): Sensitivity = Sensitivity(s.value)
}

/**
 * ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`
 */
case class Category(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Category] =
    syncF.delay(jsdk.Category.validate(inner))
}

object Category {
  def apply(cat: jsdk.Category): Category = Category(cat.value)
}

/**
 * ID of a group. Unique within a segment. Must match the regex `^[a-zA-Z0-9_.$#|@/:;=+'-]+$`
 */
case class DataSubject(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DataSubject] =
    syncF.delay(jsdk.DataSubject.validate(inner))
}

object DataSubject {
  def apply(ds: jsdk.DataSubject): DataSubject = DataSubject(ds.value)
}
