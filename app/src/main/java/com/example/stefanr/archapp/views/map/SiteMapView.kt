package com.example.stefanr.archapp.views.map

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker

class SiteMapView : BaseView(), GoogleMap.OnMarkerClickListener {

    // UI
    private var toolbarMap: Toolbar? = null
    private var mapView: MapView? = null
    private var txtViewSiteName: TextView? = null
    private var txtViewSiteDescription: TextView? = null
    private var cbSiteVisited: CheckBox? = null
    private var ivSiteImage: ImageView? = null

    // Presenter
    //inizialise the preseter
    lateinit var presenter: SiteMapPresenter

    // Map

    lateinit var map: GoogleMap


    //on create method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_maps)

        //load user interface
        InitializeUiElements()
        Initialze(savedInstanceState)
    }

    private fun Initialze(savedInstanceState: Bundle?) {
        // Sets the Map Toolbar

        // setSupportActionBar(toolbarMap)
        super.initToolbar(toolbarMap!!, true)

        // Sets the Presenter

        presenter = initPresenter(SiteMapPresenter(this)) as SiteMapPresenter

        // Create MapView Instance

        mapView!!.onCreate(savedInstanceState)

        // Initialize the Map Object (it = Google Map Object)

        mapView!!.getMapAsync {
            map = it
            map.setOnMarkerClickListener(this)
            presenter.loadSites()
        }
    }

    // Shows list of Sites
    override fun showSites(sites: List<SiteModel>) {
        presenter.doPopulateMap(map, sites)

    }

    // Shows a Site
    override fun showSite(site: SiteModel) {
        txtViewSiteName!!.text = site.name
        txtViewSiteDescription!!.text = site.description
        cbSiteVisited!!.isChecked = site.visited

        //   ivSiteImage!!.setImageBitmap(readImageFromPath(this, site.images[0]))
        Glide.with(this).load(site.images[0]).into(ivSiteImage!!)
    }

    // If Marker is clicked, this function is triggered
    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    private fun InitializeUiElements() {
        // all elements from user interface getting initialized here
        // toolbar
        //SiteName
        //SiteDescription
        //SiteImages
        //mapView

        toolbarMap = findViewById<View>(R.id.map_toolbar) as Toolbar
        txtViewSiteName = findViewById<View>(R.id.maps_tv_site_name) as TextView
        txtViewSiteDescription = findViewById<View>(R.id.maps_tv_site_description) as TextView
        cbSiteVisited = findViewById<View>(R.id.maps_cb_visited) as CheckBox
        ivSiteImage = findViewById<View>(R.id.maps_image_view) as ImageView
        mapView = findViewById<View>(R.id.maps_mapView) as MapView
    }
}