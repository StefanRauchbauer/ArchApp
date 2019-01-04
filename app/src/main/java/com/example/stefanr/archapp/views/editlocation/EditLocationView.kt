package com.example.stefanr.archapp.views.editlocation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.views.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker


class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    // Members
    lateinit var presenter: EditLocationPresenter

    //UI
    //creating all elements of the User Interface

    private var toolbarEdit: Toolbar? = null
    private var tv_lat: TextView? = null
    private var tv_lng: TextView? = null
    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_location)

        InitializeUiElements()
        Initialize(savedInstanceState)
    }


    private fun Initialize(savedInstanceState: Bundle?) {
        super.initToolbar(toolbarEdit!!, true)

        presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter


        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            it.setOnMarkerDragListener(this)
            it.setOnMarkerClickListener(this)
            presenter.doConfigureMap(it)
        }

        //output coordinates
        tv_lat!!.text = "%.6f".format(presenter.location.lat)
        tv_lng!!.text = "%.6f".format(presenter.location.lng)

    }

    // Creates the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Triggers the Menu Items, if the User clicks on them
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                presenter.doSave()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerDragStart(marker: Marker) {}

    //changes the output onMarkerDrag
    override fun onMarkerDrag(marker: Marker) {
        tv_lat!!.text = "%.6f".format(marker.position.latitude)
        tv_lng!!.text = "%.6f".format(marker.position.longitude)
    }


    //update presenter whan drag stopped
    //new coordinates are set
    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doUpdateMarker(marker)
        return false
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    private fun InitializeUiElements() {
        toolbarEdit = findViewById<View>(R.id.edit_toolbar) as Toolbar
        tv_lat = findViewById<View>(R.id.edit_tv_lat) as TextView
        tv_lng = findViewById<View>(R.id.edit_tv_lng) as TextView
        mapView = findViewById<View>(R.id.edit_location_mapView) as MapView
    }
}