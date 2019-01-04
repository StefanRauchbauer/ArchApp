package com.example.stefanr.archapp.views.site

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.helpers.readImageFromPath
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.*
import com.google.android.gms.maps.MapView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class SiteView : BaseView(), AnkoLogger {

    // UI
    private var site_toolbar: Toolbar? = null

    private var txt_siteName: EditText? = null
    private var txt_siteDescription: EditText? = null
    private var txt_siteNotes: EditText? = null
    private var txt_siteDate: EditText? = null
    private var siteRating: RatingBar? = null
    private var siteFavourite: CheckBox? = null
    private var siteVisited: CheckBox? = null

    private var mapView: MapView? = null
    private var tv_lat: TextView? = null
    private var tv_lng: TextView? = null

    private var img_siteImage1: ImageView? = null
    private var img_siteImage2: ImageView? = null
    private var img_siteImage3: ImageView? = null
    private var img_siteImage4: ImageView? = null

    // Presenter
    lateinit var presenter: SitePresenter

    // Model
    var site = SiteModel()

    //lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)

        initializeUiElements()
        initialize(savedInstanceState)
    }

    // Callback for the Result of a called Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }

    }

    // Initializes the Variables
    private fun initialize(savedInstanceState: Bundle?) {

        // Adds the appropriate Toolbar to the Activity
        super.initToolbar(site_toolbar!!, true)

        // Initialize Presenter
        presenter = initPresenter(SitePresenter(this)) as SitePresenter

        //      presenter.doRestartLocationUpdates()

        // MapView Control
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync {
            presenter.doConfigureMap(it)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

        // Image Button onClick Listener
        // click on image to select or make the image  you want to save

        img_siteImage1!!.setOnClickListener { presenter.doSelectImage(IMAGE_REQUEST_0) }
        img_siteImage2!!.setOnClickListener { presenter.doSelectImage(IMAGE_REQUEST_1) }
        img_siteImage3!!.setOnClickListener { presenter.doSelectImage(IMAGE_REQUEST_2) }
        img_siteImage4!!.setOnClickListener { presenter.doSelectImage(IMAGE_REQUEST_3) }
    }

    override fun showLatLng(site: SiteModel) {
        tv_lat!!.text = "%.6f".format(site.location.lat)
        tv_lng!!.text = "%.6f".format(site.location.lng)
    }

    override fun updateImage(imageNumber: Int, imagePath: String) {
        when (imageNumber) {
            0 -> img_siteImage1!!.setImageBitmap(readImageFromPath(this, imagePath))
            1 -> img_siteImage2!!.setImageBitmap(readImageFromPath(this, imagePath))
            2 -> img_siteImage3!!.setImageBitmap(readImageFromPath(this, imagePath))
            3 -> img_siteImage4!!.setImageBitmap(readImageFromPath(this, imagePath))
        }
    }

    // Show the Content of the UI elements
    override fun showSite(site: SiteModel) {

        // Sets the Name of the UI Elements
        txt_siteName!!.setText(site.name)
        txt_siteDescription!!.setText(site.description)
        txt_siteDate!!.setText(site.date)
        txt_siteNotes!!.setText(site.notes)
        siteVisited!!.isChecked = site.visited
        siteFavourite!!.isChecked = site.favourite
        siteRating!!.rating = site.rating

        if (site.images[0] != "") {
            // img_siteImage1!!.setImageBitmap(readImageFromPath(this, site.images[0]))
            Glide.with(this).load(site.images[0]).into(img_siteImage1!!)
        }
        if (site.images[1] != "") {
            //  img_siteImage1!!.setImageBitmap(readImageFromPath(this, site.images[1]))
            Glide.with(this).load(site.images[1]).into(img_siteImage2!!)
        }
        if (site.images[2] != "") {
            //   img_siteImage1!!.setImageBitmap(readImageFromPath(this, site.images[2]))
            Glide.with(this).load(site.images[2]).into(img_siteImage3!!)
        }
        if (site.images[3] != "") {
            //  img_siteImage1!!.setImageBitmap(readImageFromPath(this, site.images[3]))
            Glide.with(this).load(site.images[3]).into(img_siteImage4!!)
        }

        tv_lat!!.text = "%.6f".format(site.location.lat)
        tv_lng!!.text = "%.6f".format(site.location.lng)
    }



    // Creates the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Triggers the Menu Items, if the User clicks on them
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> {
                if (txt_siteName!!.text.toString().isEmpty()) {
                    toast(R.string.site_enter_site_name)
                } else {
                    presenter.doAddorSave(
                        txt_siteName!!.text.toString(),
                        txt_siteDescription!!.text.toString(),
                        txt_siteNotes!!.text.toString(),
                        siteVisited!!.isChecked,
                        txt_siteDate!!.text.toString(),
                        siteRating!!.rating,
                        siteFavourite!!.isChecked
                    )
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_share -> {
                sendMail()
            }
            R.id.item_logout -> {
                presenter.doLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendMail() {
        var subject = "My Site"
        var name = "Site: " + txt_siteName!!.text.toString()
        var description = "Description: " + txt_siteDescription!!.text.toString()

        var visited = ""
        if (siteVisited!!.isChecked) {
            visited = "visited: YES"
        } else {
            visited = "visited: NO"
        }

        var favourite = ""
        if (siteFavourite!!.isChecked) {
            favourite = "Favourite: YES"
        } else {
            favourite = "Favourite: NO"
        }

        var date = "Date: " + txt_siteDate!!.text.toString()

        var rating = ""
        if (siteRating != null) {
            rating = "Rating: " + siteRating!!.rating.toString() + " Stars"
        } else {
            rating = "No Rating"
        }


        var location = "Location: lat." + tv_lat!!.text.toString() + " / lng." + tv_lng!!.text.toString()

        var newline = System.getProperty("line.separator")

        var message = name + newline + description + newline + newline + visited + newline + favourite + newline + date + newline + rating + newline + location + newline

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        intent.type = "message/rfc822"

        startActivity(Intent.createChooser(intent, "Choose an EMail client"))
    }

    override fun setImgBitmap(imgBitmap: Bitmap) {
        img_siteImage1!!.setImageBitmap(imgBitmap)
    }



    override fun onBackPressed() {
        presenter.doCancel()
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    fun initializeUiElements() {
        // Initializes the UI Elements
        site_toolbar = findViewById<View>(R.id.site_toolbar) as Toolbar

        txt_siteName = findViewById<View>(R.id.site_et_name) as EditText
        txt_siteDescription = findViewById<View>(R.id.site_et_description) as EditText
        txt_siteNotes = findViewById<View>(R.id.site_et_additional_notes) as EditText
        txt_siteDate = findViewById<View>(R.id.site_et_date) as EditText
        siteRating = findViewById<View>(R.id.site_ratingBar) as RatingBar
        siteFavourite = findViewById<View>(R.id.site_list_favourite) as CheckBox
        siteVisited = findViewById<View>(R.id.site_cb_visited) as CheckBox

        tv_lat = findViewById<View>(R.id.site_tv_lat) as TextView
        tv_lng = findViewById<View>(R.id.site_tv_lng) as TextView
        mapView = findViewById<View>(R.id.site_map_View) as MapView

        img_siteImage1 = findViewById<View>(R.id.site_iv_image1) as ImageView
        img_siteImage2 = findViewById<View>(R.id.site_iv_image2) as ImageView
        img_siteImage3 = findViewById<View>(R.id.site_iv_image3) as ImageView
        img_siteImage4 = findViewById<View>(R.id.site_iv_image4) as ImageView
    }
}