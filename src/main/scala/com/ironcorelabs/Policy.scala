package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import cats.implicits._

/**
 * Policy grant
 * Document access granted by a policy.
 *
 * The triple (`category`, `sensitivity`, `dataSubject`) maps to a single policy rule.
 * Each policy rule may generate any number of users/groups.
 *
 * `substituteId` replaces `%USER%` in a matched policy rule.
 */
case class PolicyGrant(
  category: Option[Category],
  sensitivity: Option[Sensitivity],
  dataSubject: Option[DataSubject],
  substituteId: Option[UserId]
) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.PolicyGrant] =
    for {
      javaCat          <- category.traverse(_.toJava)
      javaSens         <- sensitivity.traverse(_.toJava)
      javaDataSub      <- dataSubject.traverse(_.toJava)
      javaSubstituteId <- substituteId.traverse(_.toJava)
    } yield new jsdk.PolicyGrant(javaCat.orNull, javaSens.orNull, javaDataSub.orNull, javaSubstituteId.orNull)
}

case class Sensitivity(id: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Sensitivity] =
    syncF.delay(jsdk.Sensitivity.validate(id))
}

object Sensitivity {
  def apply(s: jsdk.Sensitivity): Sensitivity = Sensitivity(s.value)
}

case class Category(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Category] =
    syncF.delay(jsdk.Category.validate(inner))
}

object Category {
  def apply(cat: jsdk.Category): Category = Category(cat.value)
}

case class DataSubject(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DataSubject] =
    syncF.delay(jsdk.DataSubject.validate(inner))
}

object DataSubject {
  def apply(ds: jsdk.DataSubject): DataSubject = DataSubject(ds.value)
}
