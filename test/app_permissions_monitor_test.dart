import 'package:flutter_test/flutter_test.dart';
import 'package:app_permissions_monitor/app_permissions_monitor.dart';
import 'package:app_permissions_monitor/app_permissions_monitor_platform_interface.dart';
import 'package:app_permissions_monitor/app_permissions_monitor_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAppPermissionsMonitorPlatform
    with MockPlatformInterfaceMixin
    implements AppPermissionsMonitorPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');



  @override
  Future<List<String>> detectPermissionGroupChanges(List<Map<String, dynamic>> oldPermissions) {
    // TODO: implement detectPermissionGroupChanges
    throw UnimplementedError();
  }

  @override
  Future<List> getInstalledAppsPermissionStatuses() {
    // TODO: implement getInstalledAppsPermissionStatuses
    throw UnimplementedError();
  }
}

void main() {
  final AppPermissionsMonitorPlatform initialPlatform = AppPermissionsMonitorPlatform.instance;

  test('$MethodChannelAppPermissionsMonitor is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAppPermissionsMonitor>());
  });

  test('getPlatformVersion', () async {
    AppPermissionsMonitor appPermissionsMonitorPlugin = AppPermissionsMonitor();
    MockAppPermissionsMonitorPlatform fakePlatform = MockAppPermissionsMonitorPlatform();
    AppPermissionsMonitorPlatform.instance = fakePlatform;

    expect(await appPermissionsMonitorPlugin.getPlatformVersion(), '42');
  });
}
