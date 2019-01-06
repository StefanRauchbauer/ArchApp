package com.example.stefanr.archapp.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stefanr.archapp.helpers.Converters
import com.example.stefanr.archapp.models.SiteModel


@Database(entities = arrayOf(SiteModel::class), version = 1)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun siteDao(): SiteDao
}