package com.example.approxlocation


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 34
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private val locationRequest: LocationRequest = LocationRequest.create().apply {
//        interval = 30
//        fastestInterval = 10
//        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//        maxWaitTime = 60
//    }
//    private var locationCallback: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val locationList = locationResult.locations
//            if (locationList.isNotEmpty()) {
//                //The last location in the list is the newest
//                val location = locationList.last()
//                Toast.makeText(
//                    this@MainActivity,
//                    "Got Location: " + location.toString(),
//                    Toast.LENGTH_LONG
//                )
//                    .show()
//            }
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Getting Location", Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setMessage("This app needs the Location permission, please accept to use location functionality")
                .setPositiveButton(
                    "OK"
                ) { _, _ ->
                    //Prompt the user once explanation has been shown
                    requestLocationPermission()
                }
                .create()
                .show()

        } else {
            requestLocationPermission();
        }
    }

    private fun requestLocationPermission() {
        Log.d("Pineapple", "Setting Permissions")

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("Pineapple", "$requestCode")
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d("Pineapple", "Permission Granted!")
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                if (location != null) {
                                    Log.d("Pineapple", "${location.latitude}--${location.longitude}")
                                    // use your location object
                                    // get latitude , longitude and other info from this
                                    Toast.makeText(
                                        this,
                                        "${location.latitude}--${location.longitude}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Log.d("Pineapple", "Permission was not granted")
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Pineapple", "Permission Denied")

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}