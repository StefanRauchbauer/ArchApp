package com.example.stefanr.archapp.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class SiteModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var fbId: String = "",
    var name: String = "",
    var description: String = "",
    var images: MutableList<String> = mutableListOf("", "", "", ""),
    var notes: String = "",
    var visited: Boolean = false,
    var date: String = "",
    var rating: Float = 0f,
    var favourite: Boolean = false,
    @Embedded var location: Location = Location()
) : Parcelable

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 15f
) : Parcelable