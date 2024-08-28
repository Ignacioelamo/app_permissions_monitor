package com.example.app_permissions_monitor.helpers

import android.content.Context
import android.content.pm.PackageManager

class PermissionHelper {

    companion object {

        /**
         * A map that associates permission group names with their corresponding list of permissions.
         * Each key is a group name, and the value is a list of permissions belonging to that group.
         */
        private val permissionGroupMap = mapOf(
            "ACTIVITY_RECOGNITION" to listOf("android.permission.ACTIVITY_RECOGNITION"),
            "CALENDAR" to listOf("android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"),
            "CALL_LOG" to listOf("android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG"),
            "CAMERA" to listOf("android.permission.CAMERA"),
            "CONTACTS" to listOf("android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS"),
            "LOCATION" to listOf("android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION"),
            "MICROPHONE" to listOf("android.permission.RECORD_AUDIO"),
            "NEARBY_DEVICES" to listOf("android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_ADVERTISE", "android.permission.BLUETOOTH_SCAN", "android.permission.UWB_RANGING"),
            "NOTIFICATIONS" to listOf("android.permission.POST_NOTIFICATIONS"),
            "PHONE" to listOf("android.permission.READ_PHONE_STATE", "android.permission.READ_PHONE_NUMBERS", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.ACCEPT_HANDOVER"),
            "READ_MEDIA_AURAL" to listOf("android.permission.READ_MEDIA_AUDIO"),
            "READ_MEDIA_VISUAL" to listOf("android.permission.ACCESS_MEDIA_LOCATION", "android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"),
            "SENSORS" to listOf("android.permission.BODY_SENSORS", "android.permission.BODY_SENSORS_BACKGROUND"),
            "SMS" to listOf("android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.RECEIVE_MMS"),
            "STORAGE" to listOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
        )

        /**
         * A set of permission groups that are always active or inactive.
         * These groups are always granted or denied.
         */
        private val alwaysActiveGroups = setOf("ACTIVITY_RECOGNITION", "CALENDAR", "CALL_LOG", "CONTACTS", "NEARBY_DEVICES", "NOTIFICATIONS", "PHONE", "READ_MEDIA_AURAL", "READ_MEDIA_VISUAL", "SENSORS", "SMS", "STORAGE")


        /**
         * A set of permission groups that are only active while in use.
         * These groups are granted only when the app is in use.
         */
        private val inUseGroups = setOf("CAMERA", "MICROPHONE")

        /**
         * Retrieves the permission statuses for all installed applications.
         *
         * @param context The context used to access the PackageManager.
         * @return A list of maps, where each map contains the application name, package name, and permission group statuses.
         */
        fun getAppPermissionStatuses(context: Context): List<Map<String, Any>> {
            val pm = context.packageManager
            val installedApps = pm.getInstalledApplications(0)
            val appPermissions = mutableListOf<Map<String, Any>>()

            installedApps.forEach { app ->
                val appPermissionStatus = mutableMapOf<String, Any>()
                appPermissionStatus["appName"] = app.loadLabel(pm).toString()
                appPermissionStatus["packageName"] = app.packageName

                val requestedPermissions = getRequestedPermissions(pm, app.packageName)
                val groupStatuses = evaluatePermissionGroups(pm, app.packageName, requestedPermissions)

                appPermissionStatus["permissionGroups"] = groupStatuses
                appPermissions.add(appPermissionStatus)
            }

            return appPermissions
        }

         fun detectPermissionGroupChanges(context: Context, previousPermissions: List<Map<String, Any>>): List<String> {
            val actualPermissions = getAppPermissionStatuses(context)
            val changes = mutableListOf<String>()

            val previousPermissionsMap = previousPermissions.associateBy { it["packageName"] as String }
            val actualPermissionsMap = actualPermissions.associateBy { it["packageName"] as String }

            for ((packageName, actualPermissionStatus) in actualPermissionsMap) {
                if (previousPermissionsMap.containsKey(packageName)) {
                    val previousGroups =
                        (previousPermissionsMap[packageName]?.get("permissionGroups") as Map<String, String>)
                    val actualGroups = actualPermissionStatus["permissionGroups"] as Map<String, String>
                    for ((groupName, actualStatus) in actualGroups) {
                        val previousStatus = previousGroups[groupName]
                        if (previousStatus != actualStatus) {
                            changes.add("$packageName,$groupName,$previousStatus,$actualStatus")
                        }
                    }
                } else {
                    val actualGroups = actualPermissionStatus["permissionGroups"] as Map<String, String>
                    for ((groupName, actualStatus) in actualGroups) {
                        changes.add("$packageName,$groupName,null,$actualStatus")
                    }
                }
            }

            for ((packageName, previousPermissionStatus) in previousPermissionsMap) {
                if (!actualPermissionsMap.containsKey(packageName)) {
                    val previousGroups = previousPermissionStatus["permissionGroups"] as Map<String, String>
                    for ((groupName, previousStatus) in previousGroups) {
                        changes.add("$packageName,$groupName,$previousStatus,null")
                    }
                }
            }

            return changes
        }


        /**
         * Retrieves the permissions requested by an application.
         *
         * @param pm The PackageManager to be used to obtain the package information.
         * @param packageName The package name of the application for which the requested permissions will be obtained.
         * @return A set of permissions requested by the application. If no permissions are found, an empty set is returned.
         */
        private fun getRequestedPermissions(pm: PackageManager, packageName: String): Set<String> {
            return try {
                val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                packageInfo.requestedPermissions?.toSet() ?: emptySet()
            } catch (e: PackageManager.NameNotFoundException) {
                emptySet()
            }
        }

        /**
         * Evaluates the permission groups and determines their status for a specific application.
         *
         * @param pm The PackageManager to be used to obtain the package information.
         * @param packageName The package name of the application for which the permission groups will be evaluated.
         * @param requestedPermissions A set of permissions requested by the application.
         * @return A map where each key is a permission group name and the value is the status of that group.
         */
        private fun evaluatePermissionGroups(pm: PackageManager, packageName: String, requestedPermissions: Set<String>): Map<String, String> {
            val groupStatuses = mutableMapOf<String, String>()

            permissionGroupMap.forEach { (groupName, permissions) ->
                val status = getPermissionGroupStatus(pm, packageName, groupName, permissions, requestedPermissions)
                groupStatuses[groupName] = status
            }

            // Adjusts media permissions for devices running Android 12 or lower
            adjustMediaPermissionsForLegacyVersions(groupStatuses)

            return groupStatuses
        }

        /**
         * Parameters:
         * @param pm: `PackageManager`
         * The `PackageManager` instance used to obtain package information.
         * @param packageName: `String`
         * The name of the package for which the permission groups will be evaluated.
         * @param groupName: `String`
         * The name of the permission group being evaluated.
         * @param permissions: `List<String>`
         * A list of permissions that belong to the permission group.
         * @param requestedPermissions: `Set<String>`
         * A set of permissions that the application has requested.
         *
         * Return:
         * @return `String`
         * A string representing the status of the permission group. Possible values include:
         * - `"Always"`: The permission group is always granted.
         * - `"While in use"`: The permission group is granted only while the app is in use.
         * - `"Denied"`: The permission group is denied.
         * - `"Not requested"`: The permission group is not requested.
         * - `"Unknown"`: The status of the permission group is unknown.
         */
        private fun getPermissionGroupStatus(
            pm: PackageManager,
            packageName: String,
            groupName: String,
            permissions: List<String>,
            requestedPermissions: Set<String>
        ): String {
            var isGranted = false
            var isDenied = false
            var allNotRequested = true
            var backgroundLocationGranted = false

            permissions.forEach { permission ->
                if (requestedPermissions.contains(permission)) {
                    allNotRequested = false
                    val checkStatus = pm.checkPermission(permission, packageName)
                    if (checkStatus == PackageManager.PERMISSION_GRANTED) {
                        isGranted = true
                        if (permission == "android.permission.ACCESS_BACKGROUND_LOCATION") {
                            backgroundLocationGranted = true
                        }
                    } else if (checkStatus == PackageManager.PERMISSION_DENIED) {
                        isDenied = true
                    }
                }
            }

            return when {
                groupName == "LOCATION" && backgroundLocationGranted -> "Always"
                groupName == "LOCATION" && isGranted -> "While in use"
                isGranted && alwaysActiveGroups.contains(groupName) -> "Always"
                isGranted && inUseGroups.contains(groupName) -> "While in use"
                isDenied -> "Denied"
                allNotRequested -> "Not requested"
                else -> "Unknown"
            }
        }

        /**
         * Adjusts media permissions for devices running Android 12 or lower.
         *
         * @param groupStatuses A mutable map where each key is a permission group name and the value is the status of that group.
         */
        private fun adjustMediaPermissionsForLegacyVersions(groupStatuses: MutableMap<String, String>) {
            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.S && groupStatuses["STORAGE"] == "Always") {
                groupStatuses["READ_MEDIA_AURAL"] = "Always"
                groupStatuses["READ_MEDIA_VISUAL"] = "Always"
            }
        }
    }
}
