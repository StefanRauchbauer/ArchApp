package com.example.stefanr.archapp.views.navigation

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker

class NavigationView : BaseView(), GoogleMap.OnMarkerClickListener {

    // UI
    private var toolbarNavigation: Toolbar? = null
    private var mapView: MapView? = null

    // Presenter
    lateinit var presenter: NavigationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        InitializeUiElements()
        Initialzie(savedInstanceState)
    }

    private fun Initialzie(savedInstanceState: Bundle?) {
        // Sets the Map Toolbar
        // setSupportActionBar(toolbarMap)
        super.initToolbar(toolbarNavigation!!, true)

        // Sets the Presenter
        presenter = initPresenter(NavigationPresenter(this)) as NavigationPresenter

        // MapView Control
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            presenter.doConfigureMap(it)
        }
    }

    // If Marker is clicked, this function is triggered
    override fun onMarkerClick(marker: Marker): Boolean {
        //presenter.doMarkerSelected(marker)
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
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }


    private fun InitializeUiElements() {
        // Initialize Variables
        toolbarNavigation = findViewById<View>(R.id.navigation_toolbar) as Toolbar
        mapView = findViewById<View>(R.id.navigation_navView) as MapView
    }
}