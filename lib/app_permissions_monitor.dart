
import 'dart:ffi';

import 'app_permissions_monitor_platform_interface.dart';

class AppPermissionsMonitor {
  Future<String?> getDeviceId() {
    return AppPermissionsMonitorPlatform.instance.getDeviceId();
  }

  Future<bool?> getScreenLockType() {
    return AppPermissionsMonitorPlatform.instance.getScreenLockType();
  }

  Future<bool?> getLocationStatus() {
    return AppPermissionsMonitorPlatform.instance.getLocationStatus();
  }

  Future<List<dynamic>> getInstalledAppsPermissionStatuses() {
    return AppPermissionsMonitorPlatform.instance.getInstalledAppsPermissionStatuses();
  }


}
