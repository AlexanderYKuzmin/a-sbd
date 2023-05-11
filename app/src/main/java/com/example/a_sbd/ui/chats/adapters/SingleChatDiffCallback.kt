package com.example.a_sbd.ui.chats.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.a_sbd.domain.model.Message

object SingleChatDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}