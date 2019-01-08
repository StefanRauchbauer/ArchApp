package com.example.stefanr.archapp.views

import android.content.Intent
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.createaccount.CreateAccountView
import com.example.stefanr.archapp.views.editlocation.EditLocationView
import com.example.stefanr.archapp.views.favouritelist.FavouriteListView
import com.example.stefanr.archapp.views.forgotpassword.ForgotPasswordView
import com.example.stefanr.archapp.views.login.LoginView
import com.example.stefanr.archapp.views.map.SiteMapView
import com.example.stefanr.archapp.views.navigation.NavigationView
import com.example.stefanr.archapp.views.settings.SettingsView
import com.example.stefanr.archapp.views.site.SiteView
import com.example.stefanr.archapp.views.sitelist.SiteListView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger

// Request Codes
val IMAGE_REQUEST_0 = 1
val IMAGE_REQUEST_1 = 2
val IMAGE_REQUEST_2 = 3
val IMAGE_REQUEST_3 = 4

val LOCATION_REQUEST = 5

// Enumeration
enum class VIEW {
    LOCATION, SITE, MAPS, SITELIST, LOGIN, CREATESITE, FORGOTPASSWORD, SETTINGS, FAVOURITE, NAVIGATOR
}

abstract class BaseView : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    // Utility Method for launching Activities
    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, FavouriteListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.SITE -> intent = Intent(this, SiteView::class.java)
            VIEW.MAPS -> intent = Intent(this, SiteMapView::class.java)
            VIEW.SITELIST -> intent = Intent(this, SiteListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.CREATESITE -> intent = Intent(this, CreateAccountView::class.java)
            VIEW.FORGOTPASSWORD -> intent = Intent(this, ForgotPasswordView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVOURITE -> intent = Intent(this, FavouriteListView::class.java)
            VIEW.NAVIGATOR -> intent = Intent(this, NavigationView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }

        if (view == VIEW.LOGIN) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        startActivityForResult(intent, code)
    }

    // Initializes the Presenter
    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    // Initilizes different Stuff
    fun initToolbar(toolbar: Toolbar, upEnabled: Boolean) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            toolbar.title = "$title: ${user.email}"
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
    }

    // Destroys the Presenter
    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    // Callback Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showSite(site: SiteModel) {}
    open fun showSites(sites: List<SiteModel>) {}
    open fun showLatLng(site: SiteModel) {}
    open fun showProgressBar() {}
    open fun hideProgressBar() {}
    open fun updateImage(imageNumber: Int, imagePath: String) {}
    open fun showSettingsInformations(user: FirebaseUser?, numberOfSites: Int, numberOfSitesVisited: Int) {}
    open fun setImgBitmap(imgBitmap: Bitmap) {}
}