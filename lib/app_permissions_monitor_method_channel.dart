import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'app_permissions_monitor_platform_interface.dart';

/// An implementation of [AppPermissionsMonitorPlatform] that uses method channels.
class MethodChannelAppPermissionsMonitor extends AppPermissionsMonitorPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('app_permissions_monitor');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<List<dynamic>> getInstalledAppsPermissionStatuses() async {
    final result = await methodChannel.invokeMethod('getInstalledAppsPermissionStatuses');
    return result;
  }

  @override
  Future<List<String>> detectPermissionGroupChanges(List<Map<String, dynamic>> oldPermissions) async {
    final result = await methodChannel.invokeMethod('detectPermissionGroupChanges', {'oldPermissions': oldPermissions});
    return result;
  }
}
