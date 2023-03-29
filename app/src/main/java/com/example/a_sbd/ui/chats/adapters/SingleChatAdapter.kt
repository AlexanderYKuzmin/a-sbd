package com.example.a_sbd.ui.chats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.a_sbd.databinding.ItemSingleChatDefaultInBinding
import com.example.a_sbd.databinding.ItemSingleChatDefaultOutBinding
import com.example.a_sbd.databinding.ItemSingleChatDividerBinding
import com.example.a_sbd.databinding.ItemSingleChatInBinding
import com.example.a_sbd.databinding.ItemSingleChatOutBinding
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType
import com.example.a_sbd.domain.model.MessageType.*
import com.example.a_sbd.extensions.formatToTime

class SingleChatAdapter(): ListAdapter<Message, SingleChatViewHolder>(SingleChatDiffCallback)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder {
        val binding = when(viewType) {

            ITEM_START_IN -> ItemSingleChatInBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ITEM_START_OUT -> ItemSingleChatOutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ITEM_DEFAULT_IN -> ItemSingleChatDefaultInBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ITEM_DEFAULT_OUT -> ItemSingleChatDefaultOutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            else -> ItemSingleChatDividerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        }
        return SingleChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChatViewHolder, position: Int) {
        val currentMessage = getItem(position)
        val beforeMessage = getItem(position - 1)
        with(holder.binding) {
            when (getItemViewType(position)) {
                ITEM_START_IN -> {
                    this as ItemSingleChatInBinding
                }
                ITEM_START_OUT -> holder.binding as ItemSingleChatOutBinding
                ITEM_DEFAULT_IN -> {
                    this as ItemSingleChatDefaultInBinding
                    tvMessageTextDefaultIn.text = currentMessage.text
                    tvMessageTimeDefaultIn.text = currentMessage.messageDate.formatToTime()
                        //root.background =
                }
                ITEM_DEFAULT_OUT -> holder.binding as ItemSingleChatDefaultOutBinding
                else -> holder.binding as ItemSingleChatDividerBinding
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).messageType) {
            EMPTY -> ITEM_DIVIDER
            START_IN -> ITEM_START_IN
            START_OUT -> ITEM_START_OUT
            DEFAULT_IN -> ITEM_DEFAULT_IN
            DEFAULT_OUT -> ITEM_DEFAULT_OUT
        }
    }

    companion object {
        private const val ITEM_DIVIDER = 100
        private const val ITEM_START_IN = 101
        private const val ITEM_START_OUT = 102
        private const val ITEM_DEFAULT_IN = 103
        private const val ITEM_DEFAULT_OUT = 104
    }
}