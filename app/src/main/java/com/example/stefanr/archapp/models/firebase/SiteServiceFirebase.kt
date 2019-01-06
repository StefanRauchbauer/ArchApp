package com.example.stefanr.archapp.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.example.stefanr.archapp.helpers.readImageFromPath

import com.example.stefanr.archapp.models.SiteModel
import com.example.stefanr.archapp.models.SiteService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class SiteServiceFirebase(val context: Context) : SiteService, AnkoLogger {

    val sites = ArrayList<SiteModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override suspend fun findAll(): List<SiteModel> {
        return sites
    }

    override suspend fun findById(id: Long): SiteModel? {
        val foundSite: SiteModel? = sites.find { s -> s.id == id }
        return foundSite
    }

    override suspend fun create(site: SiteModel) {
        val key = db.child("users").child(userId).child("sites").push().key
        site.fbId = key!!
        sites.add(site)
        db.child("users").child(userId).child("sites").child(key).setValue(site)
        updateImages(site)
    }

    override suspend fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { s -> s.fbId == site.fbId }
        if (foundSite != null) {
            foundSite.name = site.name
            foundSite.description = site.description
            foundSite.images = site.images
            foundSite.location = site.location
            foundSite.visited = site.visited
            foundSite.notes = site.notes
            foundSite.rating = site.rating
            foundSite.favourite = site.favourite
        }

        db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)
        if ((site.images.any { x -> x != "" && x.first() != 'h' })) {
            updateImages(site)
        }
    }

    override suspend fun delete(site: SiteModel) {
        db.child("users").child(userId).child("sites").child(site.fbId).removeValue()
        sites.remove(site)
    }

    override fun clear() {
        sites.clear()
    }

    fun updateImages(site: SiteModel) {
        for (current in 0 until site.images.count()) {
            if (site.images[current] != "") {
                val fileName = File(site.images[current])
                val imageName = fileName.name

                var imageRef = st.child(userId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, site.images[current])

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println(it.message)
                    }.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            site.images[current] = it.toString()
                            db.child("users").child(userId).child("sites").child(site.fbId).setValue(site)
                        }
                    }
                }
            }
        }
    }

    fun fetchSites(sitesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(sites) { it.getValue<SiteModel>(SiteModel::class.java) }
                sitesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        sites.clear()
        db.child("users").child(userId).child("sites").addListenerForSingleValueEvent(valueEventListener)
    }
}