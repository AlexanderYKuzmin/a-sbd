package com.example.a_sbd.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.example.a_sbd.R
import com.example.a_sbd.domain.model.DeviceSimple

class DeviceListDialogFragment(
    private val devices: List<DeviceSimple>,
) : DialogFragment() {

    var onDeviceItemClickListener: OnDeviceItemClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_device_list, null)
        //val tvName = view.findViewById<TextView>(R.id.tv_device_name)

        val adapter = DeviceAdapterHelper(requireActivity().applicationContext).getAdapter(devices)
        val listView = view.findViewById<ListView>(R.id.lv_device_list)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                onDeviceItemClickListener?.onDeviceItemClick(position)
                dismiss()
            }

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setNegativeButton(R.string.dialog_fragment_close_device_list) { dialog, _ ->
                dialog.cancel()
            }

        return alertDialogBuilder.create()
    }

    interface OnDeviceItemClickListener {
        fun onDeviceItemClick(position: Int)
    }
}