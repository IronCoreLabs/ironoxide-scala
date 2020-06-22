package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}

/**
 * Claims required to form a valid `Jwt`
 *
 * @param sub Unique user ID
 * @param pid Project ID
 * @param sid Segment ID
 * @param kid Service key ID
 * @param iat Issued time (seconds)
 * @param exp Expiration time (seconds)
 */
case class JwtClaims(
  sub: String,
  pid: Long,
  sid: String,
  kid: Long,
  iat: Long,
  exp: Long
)

object JwtClaims {
  def apply(jc: jsdk.JwtClaims): JwtClaims =
    JwtClaims(
      jc.getSub,
      jc.getPid,
      jc.getSid,
      jc.getKid,
      jc.getIat,
      jc.getExp
    )
}
