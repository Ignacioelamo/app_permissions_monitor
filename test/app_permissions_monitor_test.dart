import 'package:flutter_test/flutter_test.dart';
import 'package:app_permissions_monitor/app_permissions_monitor.dart';
import 'package:app_permissions_monitor/app_permissions_monitor_platform_interface.dart';
import 'package:app_permissions_monitor/app_permissions_monitor_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAppPermissionsMonitorPlatform
    with MockPlatformInterfaceMixin
    implements AppPermissionsMonitorPlatform {
  @override
  Future<String?> getDeviceId() => Future.value('42');

  @override
  Future<bool?> isScreenLocked() => throw UnimplementedError();

  @override
  Future<bool?> getLocationStatus() => throw UnimplementedError();

  @override
  Future<List<dynamic>> getInstalledAppsPermissionStatuses() =>
      throw UnimplementedError();

  @override
  Future<bool?> hasUsageStatsPermission() => throw UnimplementedError();

  @override
  Future<List<dynamic>> getAppUsageToday() => throw UnimplementedError();

  @override
  Future<bool?> requestUsageStatsPermission() => throw UnimplementedError();
}

void main() {
  final AppPermissionsMonitorPlatform initialPlatform =
      AppPermissionsMonitorPlatform.instance;

  test('$MethodChannelAppPermissionsMonitor is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAppPermissionsMonitor>());
  });

  test('getDeviceId delegates to platform', () async {
    final appPermissionsMonitorPlugin = AppPermissionsMonitor();
    final fakePlatform = MockAppPermissionsMonitorPlatform();
    AppPermissionsMonitorPlatform.instance = fakePlatform;

    expect(await appPermissionsMonitorPlugin.getDeviceId(), '42');
  });
}
