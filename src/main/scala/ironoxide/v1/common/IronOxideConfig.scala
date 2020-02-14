package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}
import cats.effect.Sync
import cats.implicits._
import scala.concurrent.duration.Duration

case class IronOxideConfig(
  policyCaching: PolicyCachingConfig,
  sdkOperationTimeout: Option[Duration]
) {
  // def toJava(): jsdk.IronOxideConfig =
  //   new jsdk.IronOxideConfig(policyCaching, sdkOperationTimeout)
  // private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.IronOxideConfig] =
  //   for {
  //     javaPolicyCaching <- policyCaching.toJava
  //   } syncF.delay(new jsdk.IronOxideConfig(policyCaching.toJava, sdkOperationTimeout))
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.IronOxideConfig] =
    for {
      javaPolicy <- policyCaching.toJava
      javaTimeout = sdkOperationTimeout.map(_.toJsdkDuration).orNull
    } yield new jsdk.IronOxideConfig(javaPolicy, javaTimeout)
}

object IronOxideConfig {
  def apply(): IronOxideConfig = {
    val javaConfig = new jsdk.IronOxideConfig
    val javaPolicy = javaConfig.getPolicyCachingConfig
    val javaTimeout = javaConfig.getSdkOperationTimeout.toScala
    val scalaTimeout = javaTimeout.map(d => Duration(d.getMillis, "millis"))
    IronOxideConfig(PolicyCachingConfig(javaPolicy), scalaTimeout)
  }
}

// object IronOxideConfig {
//   def apply(pc: PolicyCachingConfig, timeout: Option[java.time.Duration]): IronOxideConfig =
//     IronOxideConfig(
//       pc,
//       timeout.map(d => com.ironcorelabs.sdk.Duration.from_millis(d.get(java.time.temporal.ChronoUnit.MILLIS)))
//     )
// }
