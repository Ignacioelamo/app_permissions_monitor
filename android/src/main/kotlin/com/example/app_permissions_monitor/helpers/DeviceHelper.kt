package com.example.app_permissions_monitor.helpers

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.provider.Settings

import android.app.KeyguardManager
import android.content.Context
import android.location.LocationManager
import android.os.Build

class DeviceHelper {
    companion object{
        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String{
            return  Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun getScreenLockType(context: Context): Boolean {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    when {
                        keyguardManager.isDeviceSecure -> true
                        else -> false
                    }
                }
                else -> {
                    when {
                        keyguardManager.isKeyguardSecure -> true
                        else -> false
                    }
                }
            }
        }

        // function to check if biometric is active or not
        fun isBiometricActive(context: Context): Boolean {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            return keyguardManager.isDeviceSecure
        }

        // function to check if location is enabled or not
        fun getLocationStatus(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }



    }
}