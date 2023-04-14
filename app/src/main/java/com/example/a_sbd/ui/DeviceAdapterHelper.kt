package com.example.a_sbd.ui

import android.content.Context
import android.widget.SimpleAdapter
import com.example.a_sbd.R
import com.example.a_sbd.domain.model.DeviceSimple

class DeviceAdapterHelper(
    private val context: Context
) :  SimpleAdapterHelper {

    override var data: MutableList<Map<String, String>> = mutableListOf()
    override val from: Array<String>
        get() = arrayOf(ATTRIBUTE_NAME, ATTRIBUTE_ADDRESS)
    override val to: IntArray
        get() = intArrayOf(R.id.tv_device_name, R.id.tv_device_address)

    override val itemLayout: Int
        get() = R.layout.item_device_list

    override fun <T> getAdapter(list: List<T?>?): SimpleAdapter {
        list?.let {
            list.forEach {
                it as DeviceSimple
                val m: Map<String, String> = mapOf(from[0] to it.name!!, from[1] to it.address!!)
                data.add(m)
            }
        }
        return SimpleAdapter(context, data, itemLayout, from, to)

    }

    override fun <T> updateAdapter(list: List<T?>) {
        clear()
        list.forEach {
            it as DeviceSimple
            val m: Map<String, String> = mapOf(from[0] to it.name!!, from[1] to it.address!!)
            data.add(m)
        }
    }

    companion object {
        private const val ATTRIBUTE_NAME = "deviceName"
        private const val ATTRIBUTE_ADDRESS = "deviceAddress"
    }
}