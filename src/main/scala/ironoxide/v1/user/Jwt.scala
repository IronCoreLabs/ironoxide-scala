package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}
import cats.effect.Sync
import cats.implicits._

/**
 * IronCore JWT
 *
 * Must be either ES256 or RS256 and have a payload similar to `JwtClaims`, but could be
 * generated from an external source.
 *
 * @param jwt Raw JWT string
 * @param claims Payload of the JWT
 * @param algorithm Algorithm used by the JWT
 */
case class Jwt private (
  jwt: String,
  claims: JwtClaims,
  algorithm: String
) {
  private[ironoxide] def toJava[F[_]](implicit syncF: Sync[F]): F[jsdk.Jwt] =
    syncF.delay(jsdk.Jwt.validate(jwt))
}

object Jwt {
  def apply(j: jsdk.Jwt): Jwt =
    Jwt(j.getJwt, JwtClaims(j.getClaims), j.getAlgorithm)

  def validate[F[_]](jwt: String)(implicit syncF: Sync[F]): F[Jwt] = {
    val javaJwt = syncF.delay(jsdk.Jwt.validate(jwt))
    javaJwt.map(j => Jwt(jwt, JwtClaims(j.getClaims), j.getAlgorithm))
  }
}
