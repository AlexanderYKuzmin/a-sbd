package com.example.a_sbd.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.a_sbd.R
import com.example.a_sbd.databinding.FragmentDeviceListBinding
import com.example.a_sbd.domain.model.DeviceSimple

class DeviceListDialogFragment : Fragment() {

    var onDeviceItemClickListener: OnDeviceItemClickListener? = null

    private var _binding: FragmentDeviceListBinding? = null
    private val binding: FragmentDeviceListBinding
        get() = _binding ?: throw RuntimeException("FragmentDeviceListBinding is null")

    private val args by navArgs<DeviceListDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val devices = args.devicesSimple
        val devicesAsList = devices.toList()

        /*val devices = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //arguments?.getParcelableArrayList(EXTRA_DEVICES_LIST, DeviceSimple::class.java)
        } else {
            arguments?.getParcelableArrayList(EXTRA_DEVICES_LIST)
        } as ArrayList<DeviceSimple>*/

        val adapter = DeviceAdapterHelper(requireActivity().applicationContext).getAdapter(devicesAsList)
        val listView = view.findViewById<ListView>(R.id.lv_device_list)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                onDeviceItemClickListener?.onDeviceItemClick(position)
            }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDeviceItemClickListener) {
            onDeviceItemClickListener = context
        } else {
            throw RuntimeException("Activity must implement OnDeviceItemClickListener")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDeviceItemClickListener?.onDeviceItemClick(-1)
    }
    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.fragment_device_list, null)
        //val tvName = view.findViewById<TextView>(R.id.tv_device_name)
        Log.d(TAG, "DeviseListFragment devices size: ${devices.size}")
        val adapter = DeviceAdapterHelper(requireActivity().applicationContext).getAdapter(devices)
        val listView = view.findViewById<ListView>(R.id.lv_device_list)
        listView.adapter = adapter

        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                onDeviceItemClickListener?.onDeviceItemClick(position)
            }

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
            .setView(view)
            .setNegativeButton(R.string.dialog_fragment_close_device_list) { dialog, _ ->
                dialog.cancel()
            }

        return alertDialogBuilder.create()
    }*/

    interface OnDeviceItemClickListener {
        fun onDeviceItemClick(position: Int)
    }

    companion object {
        private const val EXTRA_DEVICES_LIST = "extra_devices_list"
        //private const val EMPTY_SYMBOL = ""

        @JvmStatic
        fun newInstance(devices: List<DeviceSimple>) =
            DeviceListDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_DEVICES_LIST, devices as ArrayList<out Parcelable>)
                }
            }
    }
}