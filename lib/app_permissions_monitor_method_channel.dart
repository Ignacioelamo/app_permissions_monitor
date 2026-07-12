import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'app_permissions_monitor_platform_interface.dart';

/// An implementation of [AppPermissionsMonitorPlatform] that uses method channels.
class MethodChannelAppPermissionsMonitor extends AppPermissionsMonitorPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('app_permissions_monitor');

  @override
  Future<String?> getDeviceId() async {
    final id = await methodChannel.invokeMethod<String>('getDeviceId');
    return id;
  }

  @override
  Future<bool?> isScreenLocked() async {
    final result = await methodChannel.invokeMethod('isScreenLocked');
    return result;
  }

  @override
  Future<bool?> getLocationStatus() async {
    final result = await methodChannel.invokeMethod('getLocationStatus');
    return result;
  }

  @override
  Future<List<dynamic>> getInstalledAppsPermissionStatuses() async {
    final result = await methodChannel.invokeMethod('getInstalledAppsPermissionStatuses');
    return result;
  }

  @override
  Future<bool?> hasUsageStatsPermission() async {
    final result = await methodChannel.invokeMethod<bool>('hasUsageStatsPermission');
    return result;
  }

  @override
  Future<List<dynamic>> getAppUsageToday() async {
    final result = await methodChannel.invokeMethod('getAppUsageToday');
    if (result == null) return [];
    return result;
  }

  @override
  Future<bool?> requestUsageStatsPermission() async {
    final result = await methodChannel.invokeMethod<bool>('requestUsageStatsPermission');
    return result;
  }
}
