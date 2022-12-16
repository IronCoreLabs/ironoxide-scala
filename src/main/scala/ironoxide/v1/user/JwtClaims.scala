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
  pid: Option[Long],
  prefixedPid: Option[Long],
  sid: Option[String],
  prefixedSid: Option[String],
  kid: Option[Long],
  prefixedKid: Option[Long],
  uid: Option[String],
  prefixedUid: Option[String],
  iat: Long,
  exp: Long
)

object JwtClaims {
  def apply(jc: jsdk.JwtClaims): JwtClaims = {
    val pid = if (jc.getPid.isPresent) Some(jc.getPid.getAsLong) else None
    val prefixedPid = if (jc.getPrefixedPid.isPresent) Some(jc.getPrefixedPid.getAsLong) else None
    val sid = if (jc.getSid.isPresent) Some(jc.getSid.get) else None
    val prefixedSid = if (jc.getPrefixedSid.isPresent) Some(jc.getPrefixedSid.get) else None
    val kid = if (jc.getKid.isPresent) Some(jc.getKid.getAsLong) else None
    val prefixedKid = if (jc.getPrefixedKid.isPresent) Some(jc.getPrefixedKid.getAsLong) else None
    val uid = if (jc.getUid.isPresent) Some(jc.getUid.get) else None
    val prefixedUid = if (jc.getPrefixedUid.isPresent) Some(jc.getPrefixedUid.get) else None
    JwtClaims(
      jc.getSub,
      pid,
      prefixedPid,
      sid,
      prefixedSid,
      kid,
      prefixedKid,
      uid,
      prefixedUid,
      jc.getIat,
      jc.getExp
    )
  }
}
