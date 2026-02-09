package com.example.app_permissions_monitor.helpers

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Process
import android.provider.Settings
import java.util.Calendar

class UsageStatsHelper {
    companion object {

        /**
         * Checks if the app has the PACKAGE_USAGE_STATS permission granted.
         *
         * @param context The application context.
         * @return true if the permission is granted, false otherwise.
         */
        fun hasUsageStatsPermission(context: Context): Boolean {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            return mode == AppOpsManager.MODE_ALLOWED
        }

        /**
         * Returns the usage stats for all apps for today (from 00:00 to now).
         * Each entry contains the packageName and minutes of foreground usage.
         *
         * @param context The application context.
         * @return A list of maps with "packageName" (String) and "minutes" (Double).
         */
        fun getAppUsageToday(context: Context): List<Map<String, Any>> {
            val usageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

            // Calculate the start of today (00:00:00)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis
            val now = System.currentTimeMillis()

            // Query usage stats for today
            val usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startOfDay,
                now
            )

            val result = mutableListOf<Map<String, Any>>()

            if (usageStatsList != null) {
                for (usageStats in usageStatsList) {
                    val totalTimeMs = usageStats.totalTimeInForeground
                    if (totalTimeMs > 0) {
                        val minutes = totalTimeMs / 60000.0
                        val entry = mapOf<String, Any>(
                            "packageName" to usageStats.packageName,
                            "minutes" to Math.round(minutes * 100.0) / 100.0 // Round to 2 decimals
                        )
                        result.add(entry)
                    }
                }
            }

            return result
        }
    }
}
