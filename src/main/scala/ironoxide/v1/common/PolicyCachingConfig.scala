package ironoxide.v1.common

import com.ironcorelabs.{sdk => jsdk}
import cats.effect.Sync

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
