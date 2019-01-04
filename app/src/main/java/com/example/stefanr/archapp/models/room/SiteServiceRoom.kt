package com.example.stefanr.archapp.models.room

import android.content.Context
import androidx.room.Room
import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.models.SiteService

import org.jetbrains.anko.coroutines.experimental.bg

class SiteServiceRoom(val context: Context) : SiteService {

    var dao: SiteDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.siteDao()

    }

    suspend override fun findAll(): List<SiteModel> {
        val deferredSites = bg {
            dao.findAll()
        }
        val sites = deferredSites.await()
        return sites
    }

    suspend override fun findById(id: Long): SiteModel? {
        val deferredSites = bg {
            dao.findById(id)
        }
        val sites = deferredSites.await()
        return sites
    }

    suspend override fun create(site: SiteModel) {
        bg {
            dao.create(site)
        }
    }

    suspend override fun update(site: SiteModel) {
        bg {
            dao.update(site)
        }
    }

    suspend override fun delete(site: SiteModel) {
        bg {
            dao.deleteSite(site)
        }
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}