package com.example.a_sbd.ui.chats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.example.a_sbd.R

class SingleChatFragment : Fragment() {

    val args: SingleChatFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[SingleChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SingleChatFragment", "arguments = ${args.name}")
        /*viewModel.isSingleChatStarted.observe(viewLifecycleOwner) {
            setFragmentResult(it)
        }*/
        setFragmentResult(true)

        //setFragmentResult("single_chat_started", bundleOf())
    }

    private fun setFragmentResult(it: Boolean) {
        requireActivity().supportFragmentManager
            .setFragmentResult("single_chat_started",
                bundleOf(
                    "isSingleChatStarted" to it,
                    "title" to if (it) args.name else ""
                )
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setFragmentResult(false)
    }

}