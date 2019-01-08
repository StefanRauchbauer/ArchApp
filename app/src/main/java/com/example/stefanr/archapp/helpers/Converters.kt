package com.example.stefanr.archapp.helpers

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {


    //this class will contain functions used to convert different Typs of Data
    @TypeConverter
    fun listToJson(value: List<String>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {

        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        val list = objects.toList()
        return list
    }
}