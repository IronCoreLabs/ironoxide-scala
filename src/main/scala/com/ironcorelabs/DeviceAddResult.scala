package com.ironcorelabs.scala.sdk

import com.ironcorelabs.{sdk => jsdk}
import java.{util => ju}

/**
 * Result from adding a new device. Can be turned into a DeviceContext for initialization.
 *
 * @param userId ID of a user
 * @param segmentId ID of a segment
 * @param devicePrivateKey asymmetric private key
 * @param signingPrivateKey signing private key specific to a device
 * @param deviceId ID of the device that was added
 * @param name name of the device that was added
 * @param created date that the device was created
 * @param lastUpdated date that the device was last updated
 */
case class DeviceAddResult(
  userId: UserId,
  segmentId: Long,
  devicePrivateKey: PrivateKey,
  signingPrivateKey: DeviceSigningPrivateKey,
  deviceId: DeviceId,
  name: Option[DeviceName],
  created: ju.Date,
  lastUpdated: ju.Date
) {
  def toDeviceContext(): DeviceContext =
    DeviceContext(userId, segmentId, devicePrivateKey, signingPrivateKey)
}

object DeviceAddResult {
  def apply(dar: jsdk.DeviceAddResult): DeviceAddResult =
    DeviceAddResult(
      UserId(dar.getAccountId),
      dar.getSegmentId,
      PrivateKey(dar.getDevicePrivateKey),
      DeviceSigningPrivateKey(dar.getSigningPrivateKey),
      DeviceId(dar.getDeviceId),
      DeviceName(dar.getName),
      dar.getCreated,
      dar.getLastUpdated
    )
}
