package ironoxide.v1.user

import com.ironcorelabs.{sdk => jsdk}
import ironoxide.v1.common.{DeviceId, DeviceName}
import java.{util => ju}

/**
 * Metadata about a user device
 *
 * @param id unique id for the device
 * @param name optional name for the device
 * @param created when the device was created
 * @param updated when the device was last updated
 * @param currentDevice true if this device instance is the one that was used to make the API request
 */
case class UserDevice(
  id: DeviceId,
  name: Option[DeviceName],
  created: ju.Date,
  updated: ju.Date,
  currentDevice: Boolean
)

object UserDevice {
  def apply(device: jsdk.UserDevice): UserDevice =
    UserDevice(
      DeviceId(device.getId),
      DeviceName(device.getName),
      device.getCreated,
      device.getLastUpdated,
      device.isCurrentDevice
    )
}
