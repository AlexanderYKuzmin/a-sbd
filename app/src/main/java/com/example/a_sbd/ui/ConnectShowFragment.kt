package com.example.a_sbd.ui

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a_sbd.R
import com.example.a_sbd.databinding.FragmentConnectShowBinding
import com.example.a_sbd.databinding.FragmentScanningShowBinding
import com.example.a_sbd.ui.MainActivity.Companion.TAG


class ConnectShowFragment : Fragment() {

    private var _binding: FragmentConnectShowBinding? = null
    private val binding: FragmentConnectShowBinding
        get() = _binding ?: throw RuntimeException("FragmentConnectShowBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "On view created ConnectShowFragment")
        val  drawable = binding.ivConnecting.drawable

        if (drawable is Animatable) drawable.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConnectShowFragment()
    }
}