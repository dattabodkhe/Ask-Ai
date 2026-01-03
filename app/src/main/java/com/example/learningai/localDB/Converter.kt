package com.example.learningai.localDB


import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return gson.toJson(list ?: emptyList<String>())
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}