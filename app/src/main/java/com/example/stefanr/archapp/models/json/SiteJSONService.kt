package com.example.stefanr.archapp.models.json

import android.content.Context

import com.example.stefanr.archapp.helpers.exists
import com.example.stefanr.archapp.helpers.read
import com.example.stefanr.archapp.helpers.writeToFile
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.models.SiteService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import java.util.*

// Global Variables
val JSON_FILE = "sites.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<SiteModel>>() {}.type

// Generates a random ID
fun generateRandomId(): Long {
    return Random().nextLong()
}

class SiteJSONService : SiteService, AnkoLogger {

    val context: Context
    var sites = mutableListOf<SiteModel>()


    // Constructor
    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    // Gets all Sites from the List
    override suspend fun findAll(): MutableList<SiteModel> {
        return sites
    }

    // Gets one Site by searching for the Site ID
    override suspend fun findById(id: Long): SiteModel? {
        val foundSite: SiteModel? = sites.find { it.id == id }
        return foundSite
    }

    // Creates and adds the Site to the List
    override suspend fun create(site: SiteModel) {
        site.id = generateRandomId()
        sites.add(site)
        serialize()
    }

    // Updates a Site
    override suspend fun update(site: SiteModel) {
        val sitesList = findAll() as ArrayList<SiteModel>
        var foundSite: SiteModel? = sitesList.find { s -> s.id == site.id }
        if (foundSite != null) {
            foundSite.name = site.name
            foundSite.images = site.images
            foundSite.visited = site.visited
            foundSite.location = site.location
            foundSite.description = site.description
            foundSite.date = site.date
            foundSite.favourite = site.favourite
            foundSite.notes = site.notes
            foundSite.rating = site.rating
        }
        serialize()
    }

    // Deletes a Site from the List
    override suspend fun delete(site: SiteModel) {
        sites.remove(site)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(
            sites,
            listType
        )
        writeToFile(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        sites = Gson().fromJson(jsonString, listType)
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
