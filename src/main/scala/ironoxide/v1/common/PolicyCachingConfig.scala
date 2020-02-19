package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}
import cats.effect.Sync

/**
 * Policy evaluation caching config
 *
 * Since policies are evaluated by the webservice, caching the result can greatly speed
 * up encrypting a document with a PolicyGrant.
 *
 * @param maxEntries maximum number of policy evaluations that will be cached by the SDK.
 *                   If the maximum number is exceeded, the cache will be cleared prior to storing the next entry.
 */
case class PolicyCachingConfig(
  maxEntries: Long
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.PolicyCachingConfig] =
    syncF.delay(new jsdk.PolicyCachingConfig(maxEntries))
}

object PolicyCachingConfig {
  def apply(pcc: jsdk.PolicyCachingConfig): PolicyCachingConfig = PolicyCachingConfig(pcc.getMaxEntries)
  def apply(): PolicyCachingConfig = PolicyCachingConfig(new jsdk.PolicyCachingConfig)
}
