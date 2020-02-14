package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}
import cats.effect.Sync
import cats.implicits._
import scala.concurrent.duration.Duration

/**
 * Top-level configuration object for IronOxide
 *
 * @param policyCaching policy caching configuration for IronOxide
 * @param sdkOperationTimeout timeout for all SDK methods
 */
case class IronOxideConfig(
  policyCaching: PolicyCachingConfig,
  sdkOperationTimeout: Option[Duration]
) {
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
