package com.example.a_sbd.extensions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

fun Activity.requestRelevantRuntimePermissions() {
    if (hasRequiredRuntimePermissions()) return
    when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> requestLocationPermission()
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> requestBluetoothPermissions()
    }
}

fun Activity.requestLocationPermission() {
    runOnUiThread {
        AlertDialog.Builder(this)
            .setTitle("Bluetooth permissions required")
            .setMessage("Starting from Android 6, the system requires apps to be granted " +
                    "location access in oder to scan for BLE devices.")
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
            .show()
    }
}

fun Activity.requestBluetoothPermissions() {
    runOnUiThread {
        AlertDialog.Builder(this)
            .setTitle("Bluetooth permissions required")
            .setMessage("Starting from Android 12, the system requires apps to be granted " +
                    "Bluetooth access in order to scan for and connect to BLE devices.")
            .setCancelable(false)
            .setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        /*Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION*/
                    ),
                    RUNTIME_PERMISSION_REQUEST_CODE
                )
            }
            .show()
    }
}

private const val PERMISSION_CONNECT_REQUEST_CODE = 101

private const val RUNTIME_PERMISSION_REQUEST_CODE = 2