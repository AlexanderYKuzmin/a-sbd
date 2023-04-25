package com.example.a_sbd.data.mapper

import com.example.a_sbd.domain.model.DeviceSimple
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

class JsonConverter @Inject constructor(){

    private val gson = Gson()

    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    fun fromJsonToListOfDevicesSimple(jsonString: String): List<DeviceSimple> {
        val arrayListDeviceSimpleType = object : TypeToken<ArrayList<DeviceSimple>>() {}.type
        return gson.fromJson(jsonString, arrayListDeviceSimpleType)
    }

    fun <T> fromJsonToSimpleDataClass(jsonString: String, typeClass: Class<T>): T {
        return gson.fromJson(jsonString, typeClass)
    }
}