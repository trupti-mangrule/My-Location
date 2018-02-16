package com.example.truptimangrule.day5challengemylocation


/**
 * Created by trupti.mangrule on 07/02/18.
 */

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.truptimangrule.myapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

public class MapsActivity : FragmentActivity(), OnMapReadyCallback, LocationListener {

     var mMap: GoogleMap? = null
    var locationManager: LocationManager?=null
    var locationListener: LocationListener?=null
    private val PERMISSION_REQUEST_CAMERA = 0
    var criteria:Criteria?= Criteria()


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationListener = this
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btn_find_me.setOnClickListener {
            //getLocation();

            val currentapiVersion = Build.VERSION.SDK_INT

            if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {

                criteria?.speedAccuracy = Criteria.ACCURACY_HIGH
                criteria?.accuracy = Criteria.ACCURACY_FINE
                criteria?.isAltitudeRequired = true
                criteria?.isBearingRequired = true
                criteria?.isSpeedRequired = true

            }
            getMyLocation()
        }
    }






    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "onMapReady: ")
        mMap = googleMap
    }

    override fun onLocationChanged(location: Location?) {
        val sydney = LatLng(location!!.latitude, location.longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18));
        val target = CameraPosition.builder().target(sydney).zoom(17f).build()
        mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(target), 2000, null)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    companion object {
        private val TAG = "MapsActivity"
    }

    private fun getMyLocation() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,""+locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER))
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 15f, locationListener)
            val provider = locationManager?.getBestProvider(criteria, true)

            val location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                Log.d("location",""+location)
                onLocationChanged(location)
            }else{
                Log.d("location","null")
            }
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission()
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the [android.Manifest.permission.CAMERA] permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private fun requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.


        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyLocation()
            } else {
                // Permission request was denied.

            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
}
