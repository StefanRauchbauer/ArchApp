package com.example.stefanr.archapp.main

import android.app.Application
import com.example.stefanr.archapp.models.SiteService
import com.example.stefanr.archapp.models.firebase.SiteServiceFirebase

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MainApp : Application(), AnkoLogger {

    lateinit var sites: SiteService

    override fun onCreate() {
        super.onCreate()
        //sites = SiteMemStore()
        //sites = SiteJSONService(applicationContext)
        //sites = SiteServiceRoom(applicationContext)
        sites = SiteServiceFirebase(applicationContext)
        info("Site started")
    }
}