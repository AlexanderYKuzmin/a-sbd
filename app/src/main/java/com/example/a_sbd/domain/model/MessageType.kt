package com.example.a_sbd.domain.model

enum class MessageType(val value: String) {
    EMPTY("empty"),
    NORMAL_IN("normal_in"),
    NORMAL_OUT("normal_out"),
    START_IN("start_in"),
    START_OUT("start_out");

    fun nextTypeForIncoming(): MessageType {
        return if (this == EMPTY || this == START_OUT || this == NORMAL_OUT) START_IN else NORMAL_IN
    }

    fun nextTypeForOutgoing(): MessageType {
        return if (this == EMPTY || this == START_IN || this == NORMAL_IN) START_OUT else NORMAL_OUT
    }
}