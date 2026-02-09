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

  Future<bool?> hasUsageStatsPermission() {
    return AppPermissionsMonitorPlatform.instance.hasUsageStatsPermission();
  }

  Future<List<dynamic>> getAppUsageToday() {
    return AppPermissionsMonitorPlatform.instance.getAppUsageToday();
  }

  Future<bool?> requestUsageStatsPermission() {
    return AppPermissionsMonitorPlatform.instance.requestUsageStatsPermission();
  }
}
