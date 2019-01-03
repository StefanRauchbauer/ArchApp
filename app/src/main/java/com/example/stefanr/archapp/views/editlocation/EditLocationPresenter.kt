package com.example.stefanr.archapp.views.editlocation

import android.content.Intent
import com.example.stefanr.archapp.models.Location
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EditLocationPresenter(view: BaseView) : BasePresenter(view) {

    // Crate new Location
    var location = Location()

    init {
        location = view.intent.extras.getParcelable("location")
    }

    // Configure the Map
    fun doConfigureMap(map: GoogleMap) {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Site")
            .snippet("GPS : " + loc.toString())
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    // Updates the Location
    fun doUpdateLocation(lat: Double, lng: Double) {
        location.lat = lat
        location.lng = lng
    }

    // Saves the Location
    fun doSave() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view?.setResult(0, resultIntent)
        view?.finish()
    }


    // Updates the Marker
    fun doUpdateMarker(marker: Marker) {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : " + loc.toString()
    }
}