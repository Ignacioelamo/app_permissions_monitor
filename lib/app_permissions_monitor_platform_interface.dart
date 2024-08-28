import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'app_permissions_monitor_method_channel.dart';

abstract class AppPermissionsMonitorPlatform extends PlatformInterface {
  /// Constructs a AppPermissionsMonitorPlatform.
  AppPermissionsMonitorPlatform() : super(token: _token);

  static final Object _token = Object();

  static AppPermissionsMonitorPlatform _instance = MethodChannelAppPermissionsMonitor();

  /// The default instance of [AppPermissionsMonitorPlatform] to use.
  ///
  /// Defaults to [MethodChannelAppPermissionsMonitor].
  static AppPermissionsMonitorPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [AppPermissionsMonitorPlatform] when
  /// they register themselves.
  static set instance(AppPermissionsMonitorPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<List<dynamic>> getInstalledAppsPermissionStatuses() {
    throw UnimplementedError('getInstalledAppsPermissionStatuses() has not been implemented.');
  }

  Future<List<String>> detectPermissionGroupChanges(List<Map<String, dynamic>> oldPermissions) {
    throw UnimplementedError('detectPermissionGroupChanges() has not been implemented.');
  }


}
