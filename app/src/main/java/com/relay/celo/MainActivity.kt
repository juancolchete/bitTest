package com.relay.celo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Create a basic User Interface programmatically (no layout XML needed)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1E1E1E")) // Dark background
        }

        val statusText = TextView(this).apply {
            text = "Celo Bitchat Relay\n\nChecking System Permissions..."
            setTextColor(Color.parseColor("#47E5BC")) // Celo Green
            textSize = 20f
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setPadding(32, 32, 32, 32)
        }

        layout.addView(statusText)
        setContentView(layout) // This makes the UI visible on the screen

        // 2. Trigger the permission requests
        requestRelayPermissions(statusText)
    }

    private fun requestRelayPermissions(statusText: TextView) {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION // Required by Android to scan for BLE
        )

        // Android 12 (API 31) and higher require explicit Bluetooth runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        // Filter out the permissions the user has already granted
        val missingPermissions = requiredPermissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            // This triggers the system popup asking the user to "Allow"
            requestPermissions(missingPermissions.toTypedArray(), 100)
        } else {
            statusText.text = "Celo Bitchat Relay\n\nPermissions Granted.\nReady to initialize Mesh!"
        }
    }

    // 3. Handle the user's response to the permission popup
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            val allGranted = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            val statusText = (findViewById<LinearLayout>(android.R.id.content).getChildAt(0) as LinearLayout).getChildAt(0) as TextView
            
            if (allGranted) {
                statusText.text = "Celo Bitchat Relay\n\nPermissions Granted.\nReady to initialize Mesh!"
                // Future step: Start the background relay service here
            } else {
                statusText.text = "Celo Bitchat Relay\n\nERROR: App cannot function without Bluetooth & Location permissions."
                statusText.setTextColor(Color.RED)
            }
        }
    }
}
