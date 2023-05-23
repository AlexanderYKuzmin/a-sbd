package com.example.a_sbd.ui.chats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.example.a_sbd.ASBDApp
import com.example.a_sbd.databinding.FragmentSingleChatBinding
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.ui.ViewModelFactory
import com.example.a_sbd.ui.chats.adapters.SingleChatAdapter
import javax.inject.Inject

class SingleChatFragment : Fragment() {

    val args: SingleChatFragmentArgs by navArgs()

    private var onMessageSendListener: OnMessageSendListener? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val singleChatViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SingleChatViewModel::class.java]
    }

    private val chatComponent by lazy {
        (requireActivity().application as ASBDApp).component.getChatContactsSubComponent()
    }

    private var _binding: FragmentSingleChatBinding? = null
    val binding: FragmentSingleChatBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*viewModel.isSingleChatStarted.observe(viewLifecycleOwner) {
            setFragmentResult(it)
        }*/
        chatComponent.inject(this)
        setFragmentResult(true)

        val contactId = args.id
        viewModelFactory.contactId = contactId

        val adapter = SingleChatAdapter(requireActivity())

        _binding!!.rvSingleChat.adapter = adapter

        singleChatViewModel.messages.observe(requireActivity()) {
            adapter.submitList(it)
        }

        singleChatViewModel.messageSentLiveData.observe(requireActivity()) {
            onMessageSendListener?.onMessageSend(it)
        }

        binding.ibtnSendMessage.setOnClickListener {
            val text = binding.etTextInput.text.toString()
            binding.etTextInput.text.clear()
            singleChatViewModel.insertMessageToDb(text)
        }

        //setFragmentResult("single_chat_started", bundleOf())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMessageSendListener) {
            onMessageSendListener = context
        } else {
            throw RuntimeException("Activity must implement OnDeviceItemClickListener")
        }
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

    interface OnMessageSendListener {
        fun onMessageSend(message: Message)
    }

}