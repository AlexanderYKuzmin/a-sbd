package com.example.a_sbd.ui.chats.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.a_sbd.domain.model.ChatContact

object ChatContactDiffCallback : DiffUtil.ItemCallback<ChatContact>(){
    override fun areItemsTheSame(oldItem: ChatContact, newItem: ChatContact): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatContact, newItem: ChatContact): Boolean {
        return oldItem == newItem
    }
}