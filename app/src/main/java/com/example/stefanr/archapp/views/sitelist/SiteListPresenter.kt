package com.example.stefanr.archapp.views.sitelist


import com.example.stefanr.archapp.main.MainApp
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.example.stefanr.archapp.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SiteListPresenter(view: BaseView) : BasePresenter(view) {

    //Firebase references
    private var mAuth: FirebaseAuth? = null

    init {
        app = view.application as MainApp
        mAuth = FirebaseAuth.getInstance()
    }

    fun doAddSite() {
        view?.navigateTo(VIEW.SITE)
    }

    fun doEditSite(site: SiteModel) {
        view?.navigateTo(VIEW.SITE, 0, "site_edit", site)
    }

    fun doShowSitesMap() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun loadSites() {
        async(UI) {
            view?.showSites(app.sites.findAll())
        }
    }

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doShowFavourites() {
        view?.navigateTo(VIEW.FAVOURITE)
    }

    fun doShowNavigator() {
        view?.navigateTo(VIEW.NAVIGATOR)
    }

    fun doShowSearch() {
    }

    fun doLogout() {
        mAuth!!.signOut()
        app.sites.clear()
        view?.navigateTo(VIEW.LOGIN)
    }


}