package com.example.a_sbd.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import android.widget.SimpleAdapter
import com.example.a_sbd.R
import com.example.a_sbd.domain.model.DeviceSimple

interface SimpleAdapterHelper {
    var data: MutableList<Map<String, String>>

    val from: Array<String>
    val to: IntArray

    val itemLayout: Int

    //fun getAdapter(): SimpleAdapter

    fun <T> getAdapter(list: List<T>?): SimpleAdapter

    fun <T> updateAdapter(list: List<T>)

    fun clear() {
        data.clear()
    }
}