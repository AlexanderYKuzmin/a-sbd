package com.example.a_sbd.ui.chats.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.a_sbd.R
import com.example.a_sbd.databinding.ItemSingleChatDefaultInBinding
import com.example.a_sbd.databinding.ItemSingleChatDefaultOutBinding
import com.example.a_sbd.databinding.ItemSingleChatDividerBinding
import com.example.a_sbd.databinding.ItemSingleChatInBinding
import com.example.a_sbd.databinding.ItemSingleChatOutBinding
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType.*
import com.example.a_sbd.extensions.formatToTime

class SingleChatAdapter(
    private val context: Context
): ListAdapter<Message, SingleChatViewHolder>(SingleChatDiffCallback)  {
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
        val beforeMessage = if (position > 0) getItem(position - 1) else null
        with(holder.binding) {
            when (getItemViewType(position)) {
                ITEM_START_IN -> {
                    this as ItemSingleChatInBinding
                    tvMessageTextStartIn.text = currentMessage.text
                    tvMessageTimeStartIn.text = currentMessage.messageDate.formatToTime()
                }
                ITEM_START_OUT -> {
                    this as ItemSingleChatOutBinding
                    tvMessageTextStartOut.text = currentMessage.text
                    tvMessageTimeStartOut.text = currentMessage.messageDate.formatToTime()
                    ivDepartedStartOut.visibility =
                        if(currentMessage.isDeparted) View.VISIBLE else View.INVISIBLE
                }
                ITEM_DEFAULT_IN -> {
                    this as ItemSingleChatDefaultInBinding
                    tvMessageTextDefaultIn.text = currentMessage.text
                    tvMessageTimeDefaultIn.text = currentMessage.messageDate.formatToTime()
                        //root.background =
                }
                ITEM_DEFAULT_OUT -> {
                    this as ItemSingleChatDefaultOutBinding
                    tvMessageTextDefaultOut.text = currentMessage.text
                    tvMessageTimeDefaultOut.text = currentMessage.messageDate.formatToTime()
                    ivDepartedDefaultOut.visibility =
                        if(currentMessage.isDeparted) View.VISIBLE else View.INVISIBLE
                }
                else -> this as ItemSingleChatDividerBinding
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).messageType) {
            EMPTY -> ITEM_DIVIDER
            START_IN -> ITEM_START_IN
            START_OUT -> ITEM_START_OUT
            NORMAL_IN -> ITEM_DEFAULT_IN
            NORMAL_OUT -> ITEM_DEFAULT_OUT
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