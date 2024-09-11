package com.example.app_permissions_monitor

import androidx.annotation.NonNull
import android.content.Context
import android.os.Build
import com.example.app_permissions_monitor.helpers.DeviceHelper


import com.example.app_permissions_monitor.helpers.PermissionHelper


import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.security.Permission

/** AppPermissionsMonitorPlugin */
class AppPermissionsMonitorPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "app_permissions_monitor")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when (call.method) {
        "getDeviceId" -> {
          val deviceId = DeviceHelper.getDeviceId(context)
            result.success(deviceId)
        }
        "getScreenLockType" -> {
            val screenLockType = DeviceHelper.getScreenLockType(context)
            result.success(screenLockType)
        }
        "getLocationStatus" -> {
            val locationEnabled = DeviceHelper.getLocationStatus(context)
            result.success(locationEnabled)
        }
        "getInstalledAppsPermissionStatuses" -> {
            val appPermissions = PermissionHelper.getAppPermissionStatuses(context)
            result.success(appPermissions)
        }
        else -> {
          result.notImplemented()
        }
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
