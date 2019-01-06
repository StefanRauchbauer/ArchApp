package com.example.stefanr.archapp.views.map


import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SiteMapPresenter(view: BaseView) : BasePresenter(view) {

    // Populates the Map with Markers
    fun doPopulateMap(map: GoogleMap, sites: List<SiteModel>) {

        map.uiSettings.isZoomControlsEnabled = true

        sites.forEach {
            val loc = LatLng(it.location.lat, it.location.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.location.zoom))
        }
    }

    // Shows the Site of the clicked Marker
    fun doMarkerSelected(marker: Marker) {
        async(UI) {


            val site = marker.tag as SiteModel
            if (site != null) view?.showSite(site)
        }
    }

    // Shows all the Sites
    fun loadSites() {
        async(UI) {
            view?.showSites(app.sites.findAll())
        }

    }

}