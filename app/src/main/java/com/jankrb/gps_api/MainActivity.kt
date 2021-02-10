package com.jankrb.gps_api

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestCodeGPSPermission = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            askForGPSPermission() // Request permission, if has no permissions
        }

        // Create GPS Location Manager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var getCoordsBtn: Button = findViewById(R.id.get_coords_btn)
        getCoordsBtn.setOnClickListener {
            Log.i("GPS", "Requesting GPS Data...")
            getLastKnownLocation()
        }
    }

    /**
     * Request gps permission
     */
    private fun askForGPSPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestCodeGPSPermission
        )
    }

    /**
     * Callback function for func askForGPSPermission
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == requestCodeGPSPermission && grantResults.isNotEmpty()) { // Callback is not empty
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)  {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show() // Callback permission is denied
            }
        }
    }

    /**
     * Get Last Position
     */
    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            askForGPSPermission() // Request permission, if has no permissions
        }

        var resultView: TextView = findViewById(R.id.results_text)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location->
                if (location != null) {
                    resultView.text = "Result: " + location.latitude.toString() + " : " + location.longitude.toString()
                }
            }
            .addOnFailureListener {
                resultView.text = "Error: ${it.printStackTrace()}"
            }
    }
}

