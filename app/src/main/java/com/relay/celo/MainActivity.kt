package com.relay.celo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // This forces Android to load the UI from the XML file we just created
        setContentView(R.layout.activity_main)

        // Find the text view by the ID we gave it in the XML
        statusText = findViewById(R.id.statusText)

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
            statusText.text = "Requesting permissions..."
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
