package com.example.stefanr.archapp.models.mem


import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.models.SiteService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

// Memory Store of the Sites
class SiteMemStore : SiteService, AnkoLogger {

    // List of Sites
    val sites = ArrayList<SiteModel>()

    // Returns the Site List
    override suspend fun findAll(): List<SiteModel> {
        return sites
    }

    // Gets one Site by searching for the Site ID
    override suspend fun findById(id: Long): SiteModel? {
        val foundSite: SiteModel? = sites.find { it.id == id }
        return foundSite
    }

    // Creates a new Site
    override suspend fun create(site: SiteModel) {
        site.id = getId()
        sites.add(site)
        logAll()
    }

    // Updates a Site
    override suspend fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { s -> s.id == site.id }
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

            logAll()
        }
    }

    // Logs the Sites
    internal fun logAll() {
        sites.forEach { info("${it}") }
    }

    // Deletes a Site
    override suspend fun delete(site: SiteModel) {
        sites.remove(site)
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}