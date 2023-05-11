package com.example.a_sbd.ui

import android.graphics.drawable.Animatable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_sbd.R
import com.example.a_sbd.databinding.FragmentDeviceListBinding
import com.example.a_sbd.databinding.FragmentScanningShowBinding


class ScanningShowFragment : Fragment() {

    private var _binding: FragmentScanningShowBinding? = null
    private val binding: FragmentScanningShowBinding
        get() = _binding ?: throw RuntimeException("FragmentDeviceListBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanningShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val  drawable = binding.ivScanning.drawable

        if (drawable is Animatable) drawable.start()
    }



    companion object {

        @JvmStatic
        fun newInstance() = ScanningShowFragment()

    }
}