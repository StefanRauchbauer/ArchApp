package com.example.stefanr.archapp.views.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import com.example.stefanr.archapp.R
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BaseView
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.AnkoLogger

class SettingsView : BaseView(), AnkoLogger {

    // UI
    private var settings_toolbar: Toolbar? = null
    private var email: TextView? = null
    private var number_of_sites: TextView? = null
    private var number_visited: TextView? = null

    // Site Model
    var site = SiteModel()


    // Presenter
    lateinit var presenter: SettingsPresenter


    private fun Initialize() {
        // Adds the appropriate Toolbar to the Activity
        super.initToolbar(settings_toolbar!!, true)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        presenter.showAccountInformations()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        InitializeUiElements()
        Initialize()
    }

    override fun showSettingsInformations(user: FirebaseUser?, numberOfSites: Int, numberOfSitesVisited: Int) {
        email!!.text = user!!.email
        number_of_sites!!.text = numberOfSites.toString()
        number_visited!!.text = numberOfSitesVisited.toString()
    }

    private fun InitializeUiElements() {
        settings_toolbar = findViewById<View>(R.id.settings_toolbar) as Toolbar
        email = findViewById<View>(R.id.settings_email) as TextView

        number_of_sites = findViewById<View>(R.id.settings_total_number_of_sites) as TextView
        number_visited = findViewById<View>(R.id.settings_number_visited) as TextView
    }

}