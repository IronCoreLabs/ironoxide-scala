package com.ironcorelabs.scala.sdk

import cats.effect.Sync
import com.ironcorelabs.{sdk => jsdk}
import cats.implicits._

/**
 * Document access granted by a policy.
 * The triple (`category`, `sensitivity`, `dataSubject`) maps to a single policy rule.
 * Each policy rule may generate any number of users/groups.
 *
 * @param category Optional description of the type of data
 * @param sensitivity Optional description of how restricted access to the data should be
 * @param dataSubject Optional description of with whom the data is associated
 * @param substituteId Optional UserId that will replace `%USER%` in a matched policy rule
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

/**
 * A policy's indicator of how restricted access to the data should be.
 * @example "RESTRICTED", "CLASSIFIED", "INTERNAL", etc.
 */
case class Sensitivity(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Sensitivity] =
    syncF.delay(jsdk.Sensitivity.validate(inner))
}

object Sensitivity {
  def apply(s: jsdk.Sensitivity): Sensitivity = Sensitivity(s.value)
}

/**
 * A policy's indicator of the broad type of data.
 * @example "HEALTH", "PII", etc.
 */
case class Category(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Category] =
    syncF.delay(jsdk.Category.validate(inner))
}

object Category {
  def apply(cat: jsdk.Category): Category = Category(cat.value)
}

/**
 * A policy's indicator of with whom the data is associated.
 * @example "PATIENT", "EMPLOYEE", etc.
 */
case class DataSubject(inner: String) {
  private[sdk] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.DataSubject] =
    syncF.delay(jsdk.DataSubject.validate(inner))
}

object DataSubject {
  def apply(ds: jsdk.DataSubject): DataSubject = DataSubject(ds.value)
}
