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

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Force a modern Material Theme so the system permission dialog doesn't break
        setTheme(android.R.style.Theme_Material_NoActionBar)
        super.onCreate(savedInstanceState)

        // 2. Setup the background layout
        val rootParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#1E1E1E"))
            layoutParams = rootParams
        }

        // 3. Setup the text WITH explicit layout dimensions so it cannot be invisible
        statusText = TextView(this).apply {
            text = "Initializing Celo Relay..."
            setTextColor(Color.parseColor("#47E5BC"))
            textSize = 22f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        layout.addView(statusText)
        setContentView(layout)

        // 4. Safely request permissions and print any crashes to the screen
        try {
            requestRelayPermissions()
        } catch (e: Exception) {
            statusText.text = "Error caught:\n${e.message}"
            statusText.setTextColor(Color.RED)
        }
    }

    private fun requestRelayPermissions() {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val missingPermissions = requiredPermissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            statusText.text = "Requesting ${missingPermissions.size} permissions..."
            requestPermissions(missingPermissions.toTypedArray(), 100)
        } else {
            statusText.text = "Permissions Granted!\nReady for Mesh."
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
                statusText.text = "Permissions Granted!\nReady for Mesh."
                statusText.setTextColor(Color.parseColor("#47E5BC"))
            } else {
                statusText.text = "ERROR: Permissions denied.\nApp cannot function."
                statusText.setTextColor(Color.RED)
            }
        }
    }
}
