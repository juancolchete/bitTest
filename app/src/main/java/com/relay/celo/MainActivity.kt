package com.relay.celo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    // Safely store the text view here so we can update it anywhere without crashing
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Explicitly force the layout to stretch across the entire screen
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1E1E1E")) // Dark Grey
            this.layoutParams = layoutParams
        }

        // 2. Create the status text
        statusText = TextView(this).apply {
            text = "Celo Bitchat Relay\n\nChecking System Permissions..."
            setTextColor(Color.parseColor("#47E5BC")) // Bright Celo Green
            textSize = 20f
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setPadding(32, 32, 32, 32)
        }

        // 3. Draw the UI
        layout.addView(statusText)
        setContentView(layout)

        // 4. Trigger the system permission popups
        requestRelayPermissions()
    }

    private fun requestRelayPermissions() {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Android 12+ (API 31+) needs specific Bluetooth runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val missingPermissions = requiredPermissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            // Trigger the OS popup
            requestPermissions(missingPermissions.toTypedArray(), 100)
        } else {
            statusText.text = "Celo Bitchat Relay\n\nPermissions Granted.\nReady to initialize Mesh!"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == 100) {
            val allGranted = grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            
            if (allGranted) {
                statusText.text = "Celo Bitchat Relay\n\nPermissions Granted.\nReady to initialize Mesh!"
            } else {
                statusText.text = "Celo Bitchat Relay\n\nERROR: App cannot function without Bluetooth & Location permissions."
                statusText.setTextColor(Color.RED)
            }
        }
    }
}
