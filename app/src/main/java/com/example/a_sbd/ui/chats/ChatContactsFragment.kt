package com.example.a_sbd.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.a_sbd.ASBDApp
import com.example.a_sbd.R
import com.example.a_sbd.databinding.FragmentChatContactsBinding
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.ui.ViewModelFactory
import com.example.a_sbd.ui.chats.adapters.ChatContactsAdapter
import javax.inject.Inject

class ChatContactsFragment : Fragment() {

    val hasAppComponent: HasAppComponent? = null

    private var _binding: FragmentChatContactsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val chatContactsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatContactsViewModel::class.java]
    }

    private val chatComponent by lazy {
        (requireActivity().application as ASBDApp).component.getChatContactsSubComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatContactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chatComponent.inject(this)
        /*binding.btn.setOnClickListener {
            Log.d("Fragment", "btn************")
            requireActivity().supportFragmentManager.setFragmentResult("button_pressed", bundleOf())
            setFragmentResult("fdf", bundleOf())
        }*/

        val adapter = ChatContactsAdapter(requireActivity())
        adapter.onChatClickListener = object : ChatContactsAdapter.OnChatClickListener {
            override fun onChatClick(id: Long, name: String) {
                launchSingleChatFragment(bundleOf("id" to id, "name" to name))
            }
        }

        binding.rvChatContacts.adapter = adapter

        chatContactsViewModel.chatContacts.observe(requireActivity()) {
            adapter.submitList(it)
        }

        binding.fbtnAddContact.setOnClickListener {
            launchPhoneBookContactsDialog()
        }
    }

    private fun launchPhoneBookContactsDialog() {
        //findNavController().navigate()
        val alertDialog = PhoneBookDialogFragment().apply {
            onPhoneBookContactClickListener = object : PhoneBookDialogFragment.OnPhoneBookContactClickListener {
                override fun onPhoneBookContactClick(name: String) {
                    //chatContactsViewModel.addContact(ChatContact(id = 10, firstName = "Новый", lastName = name, phoneNumber = "3412 9555"))
                }
            }
        }
        alertDialog.show(requireActivity().supportFragmentManager.beginTransaction(), null)
    }

    private fun launchSingleChatFragment(args: Bundle) {
        findNavController().navigate(R.id.single_chat, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface HasAppComponent {
        fun getAppComponent()
    }
}