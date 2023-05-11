package com.example.a_sbd.ui.chats.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.a_sbd.databinding.ItemChatContactBinding
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.extensions.formatToDateTime

class ChatContactsAdapter(
    context: Context
) : ListAdapter<ChatContact, ChatContactViewHolder>(ChatContactDiffCallback) {

    var onChatClickListener: OnChatClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatContactViewHolder {
        val binding = ItemChatContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        //val v = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_contact, parent, false)
        return ChatContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatContactViewHolder, position: Int) {
        val chatContact = getItem(position)
        with(holder.binding) {
            with(chatContact) {
                tvFirstName.text = firstName
                tvLastName.text = lastName
                tvLastMessage.text = if (messages.isNotEmpty()) messages[messages.lastIndex].text else ""
                tvMessageDate.text = messages[messages.lastIndex].messageDate.formatToDateTime()
                root.setOnClickListener {
                    onChatClickListener?.onChatClick(chatContact)
                }
            }
        }
    }

    interface OnChatClickListener {
        fun onChatClick(chatContact: ChatContact)
    }
}