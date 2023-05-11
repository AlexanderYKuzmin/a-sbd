package com.example.a_sbd.data.mapper

import android.util.Log
import com.example.a_sbd.data.database.model.ChatContactDb
import com.example.a_sbd.data.database.model.ChatContactWithMessages
import com.example.a_sbd.data.database.model.MessageDb
import com.example.a_sbd.domain.model.ChatContact
import com.example.a_sbd.domain.model.Message
import com.example.a_sbd.domain.model.MessageType
import com.example.a_sbd.extensions.formatToDateTimeUpToSeconds
import com.example.a_sbd.extensions.parseFromDateDb
import com.example.a_sbd.ui.MainActivity.Companion.TAG

class TransformationMapper {
    fun mapContactDbToEntity(contactDb: ChatContactDb): ChatContact {
        return ChatContact(
            contactDb.id,
            contactDb.firstName,
            contactDb.lastName,
            contactDb.phoneNumber,
            listOf()
        )
    }

    fun mapMessageDbToEntity(messageDb: MessageDb): Message {
        Log.d(TAG, "MessageDB: $messageDb")
        return Message(
                messageDb.id,
                messageDb.text,
                MessageType.valueOf(messageDb.type.uppercase()),
                messageDb.date.parseFromDateDb(),
                messageDb.isDeparted == 1,
                messageDb.contactId
            )
    }

    fun mapContactDbWithMessagesDbToChatContact(contactDb: ChatContactWithMessages): ChatContact {
        return  ChatContact(
            contactDb.contact.id,
            contactDb.contact.firstName,
            contactDb.contact.lastName,
            contactDb.contact.phoneNumber,
            contactDb.messages.map {
                mapMessageDbToEntity(it)
            }
        )
    }

    fun mapContactToContactDb(contact: ChatContact): ChatContactDb {
        return ChatContactDb(
            contact.id,
            contact.firstName,
            contact.lastName,
            contact.phoneNumber
        )
    }

    fun mapMessageToMessageDb(message: Message): MessageDb {
        return MessageDb(
            text = message.text,
            type = message.messageType.value,
            date = message.messageDate.formatToDateTimeUpToSeconds(),
            isDeparted = if (message.isDeparted) 1 else 0,
            contactId = message.contactId
        )
    }

    fun mapContactsToContactDbs(contacts: Array<ChatContact>): Array<ChatContactDb> {
        return contacts.map {
            mapContactToContactDb(it)
        }.toTypedArray()
    }

    fun mapMessagesToMessageDbs(messages: Array<Message>): Array<MessageDb> {
        return messages.map {
            mapMessageToMessageDb(it)
        }.toTypedArray()
    }
}