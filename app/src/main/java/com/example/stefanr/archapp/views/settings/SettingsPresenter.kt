package com.example.stefanr.archapp.views.settings


import com.example.stefanr.archapp.views.BasePresenter
import com.example.stefanr.archapp.views.BaseView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SettingsPresenter(view: BaseView) : BasePresenter(view) {

    var mAuth=FirebaseAuth.getInstance()


    fun showAccountInformations() {
        async(UI) {
            var user = mAuth!!.currentUser
            var sitesAmount = app.sites.findAll().size
            var sitesVisited = 0
            var allSites = app.sites.findAll()

            //counting how much sites are visited
            for (site in allSites.indices) {
                if (allSites[site].visited) {
                    sitesVisited++
                }
            }
            view?.showSettingsInformations(user, sitesAmount, sitesVisited)
        }

    }
}