package com.example.stefanr.archapp.views.navigation

import android.annotation.SuppressLint
import com.example.stefanr.archapp.helpers.checkLocationPermissions
import com.example.stefanr.archapp.helpers.createDefaultLocationRequest
import com.example.stefanr.archapp.helpers.isCameraPermissionGranted
import com.example.stefanr.archapp.helpers.isPermissionGranted
import com.example.stefanr.archapp.models.Location
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class NavigationPresenter(view: BaseView) : BasePresenter(view) {


    // Google Map
    var map: GoogleMap? = null

    // Location
    var defaultLocation = Location(44.446478, 13.327602, 15f)
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()

    // Sites
    lateinit var sites: List<SiteModel>


    // Initializes the Components
    init {
        async(UI) {
            sites = app.sites.findAll()
        }
        if (checkLocationPermissions(view))
            doSetCurrentLocation()
    }


    // Sets the Current Location if the Permission is Granted
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {

            if (it != null)
            {
                locationUpdate(Location(it.latitude, it.longitude))
            } else {
                locationUpdate(Location(defaultLocation.lat, defaultLocation.lng))
            }

        }
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // result from permission request if != null
        //permission is granted and location is set to current location
        if (isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation)
        }

        if (isCameraPermissionGranted(requestCode, grantResults)) {

        } else {

        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        locationService.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    // Configure Map
    fun doConfigureMap(m: GoogleMap) {
        map = m
    }

    // Update the Location
    fun locationUpdate(location: Location) {

        // Clears the map and sets Zoom option
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true


        sites.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map?.addMarker(options)?.tag = it
        }

        // Sets Marker Options and adds it to the Map
        val options = MarkerOptions().title("My Location").position(LatLng(location.lat, location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.lng), location.zoom))
    }
}