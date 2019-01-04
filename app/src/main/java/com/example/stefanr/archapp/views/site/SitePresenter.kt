package com.example.stefanr.archapp.views.site

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AlertDialog
import com.example.stefanr.archapp.helpers.*
import com.example.stefanr.archapp.models.Location
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.*

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SitePresenter(view: BaseView) : BasePresenter(view) {

    // Google Map
    var map: GoogleMap? = null

    //Firebase references
    var mAuth: FirebaseAuth? = null

    // Site Model
    var site = SiteModel()

    // Member
    var edit = false
    var mImagePath: String = ""

    // Location
    //location map starts at
    var defaultLocation = Location(44.446478, 13.327602, 15f)

    //creating a FusedLocationProviderClient object to interact with location provider
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    val locationRequest = createDefaultLocationRequest()


    // Initializes the Components
    init
    {
        mAuth = FirebaseAuth.getInstance()
        //check if there is already a site to start at or if not start at current position
        if (view.intent.hasExtra("site_edit"))
        {
            edit = true
            site = view.intent.extras.getParcelable("site_edit")
            view.showSite(site)
        } else
        {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
    }

    // Sets the Current Location if the Permission is Granted
    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            //it is smilar to the task-> in registration
            //if it is not null there was a succesfull request to get the current location
            //if there was an error or we dont have permission we set default location
            if (it != null) {
                locationUpdate(Location(it.latitude, it.longitude))
            } else {
                locationUpdate(Location(defaultLocation.lat, defaultLocation.lng))
            }
            // locationUpdate(Location(it.latitude, it.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates()
    {
        val locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult?)
            {
                if (locationResult != null)
                {
                    val l = locationResult.locations.last()
                    locationUpdate(Location(l.latitude, l.longitude))
                }
            }
        }
        if (!edit)
        {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
            locationService.removeLocationUpdates(locationCallback)
        }
    }


    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //ask for permission if granted ask for current location
        //else set default location
        if (isPermissionGranted(requestCode, grantResults))
        {
            doSetCurrentLocation()
        }
        else
        {
            locationUpdate(defaultLocation)
        }
    }

    // Configure Map
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(site.location)
    }

    // Update the Location
    fun locationUpdate(location: Location) {

        // Set the Location
        site.location = location
        site.location.zoom = 15f

        // Clears the map and sets Zoom option
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true

        // Sets Marker Options and adds it to the Map
        val options = MarkerOptions().title(site.name).position(LatLng(site.location.lat, site.location.lng))
        map?.addMarker(options)
        map?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(site.location.lat, site.location.lng),
                site.location.zoom
            )
        )

        // Shows a Site
        //view?.showSite(site)
        view?.showLatLng(site)

    }


    // Add or Saves a Site
    fun doAddorSave( name: String, description: String, notes: String, visitedFlag: Boolean, date: String, ratingStars: Float, favoriteFlag: Boolean)
    {
        site.name = name
        site.visited = visitedFlag
        site.description = description
        site.notes = notes
        site.date = date
        site.rating = ratingStars
        site.favourite = favoriteFlag

        async(UI) {
            if (edit) {
                app.sites.update(site)
            } else {
                app.sites.create(site)
            }
            view?.finish()
        }

    }

    // Closes the current Activity and goes back to the previous one
    fun doCancel()
    {
        view?.finish()
    }


    // Deletes a specific Site
    fun doDelete()
    {
        async(UI) {
            app.sites.delete(site)
            view?.finish()
        }

    }

    fun showPictureDialog(imageNumber: Int)
    {
        val pictureRequest = AlertDialog.Builder(view!!)
        pictureRequest.setTitle("Select you Option")
        val pictureDialogItems = arrayOf("Choose photo from gallery", "Take a picture with camera")
        pictureRequest.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary(imageNumber)
                1 -> takePhotoFromCamera(imageNumber)
            }
        }
        pictureRequest.show()
    }


    fun choosePhotoFromGallary(imageNumber: Int) {
        showImagePicker(view!!, imageNumber)
    }

    fun takePhotoFromCamera(imageNumber: Int) {
        if (checkCameraPermissions(view!!)) {
            if (checkStoragePermissions(view!!)) {
                dispatchTakePictureIntent(view!!, imageNumber)
            }
        }
    }

    // Selects a Image
    fun doSelectImage(imageNumber: Int) {
        showPictureDialog(imageNumber)
        //showImagePicker(view!!, imageNumber)
        //getIMageFromCamera(view!!, imageNumber)

    }

    // Sets the Location
    fun doSetLocation() {
        view?.navigateTo(
            VIEW.LOCATION,
            LOCATION_REQUEST,
            "location",
            Location(site.location.lat, site.location.lng, site.location.zoom)
        )
    }


    private fun saveBitmap(bitmap: Bitmap?, file: File) {
        if (bitmap != null) {
            try {
                var outputStream: FileOutputStream? = null
                try {
                    outputStream = FileOutputStream(file)
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        outputStream
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        outputStream?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Callback for the Result of a called Activity
    // Example: If a User requests a Image, he gets back a Image object
    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        var path: File? = null
        if (data.data == null) {
            val bm = data.extras.get("data") as Bitmap
            path = createImageFile(view!!)
            saveBitmap(bm, path)
        }
        when (requestCode) {
            IMAGE_REQUEST_0 -> {
                site.images[0] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(0, site.images[0])
            }
            IMAGE_REQUEST_1 -> {
                site.images[1] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(1, site.images[1])
            }
            IMAGE_REQUEST_2 -> {
                site.images[2] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(2, site.images[2])
            }
            IMAGE_REQUEST_3 -> {
                site.images[3] = data.data?.toString() ?: path!!.absolutePath; view?.updateImage(3, site.images[3])
            }
            LOCATION_REQUEST -> {
                val location = data.extras.getParcelable<Location>("location")
                site.location = location
                locationUpdate(location)
            }
        }
    }

    // If Lougout Menu Item clicked, the User logs out and the Login Activity starts
    fun doLogout() {
        mAuth!!.signOut()
        app.sites.clear()
        view?.navigateTo(VIEW.LOGIN)
    }
}