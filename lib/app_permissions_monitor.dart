
import 'app_permissions_monitor_platform_interface.dart';

class AppPermissionsMonitor {
  Future<String?> getPlatformVersion() {
    return AppPermissionsMonitorPlatform.instance.getPlatformVersion();
  }

  Future<List<dynamic>> getInstalledAppsPermissionStatuses() {
    return AppPermissionsMonitorPlatform.instance.getInstalledAppsPermissionStatuses();
  }

  Future<List<String>> detectPermissionGroupChanges(List<Map<String, dynamic>> oldPermissions) {
    return AppPermissionsMonitorPlatform.instance.detectPermissionGroupChanges(oldPermissions);
  }
}
