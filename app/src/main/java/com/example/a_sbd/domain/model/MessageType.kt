package com.example.a_sbd.domain.model

enum class MessageType(val value: String) {
    EMPTY("empty"),
    NORMAL_IN("normal_in"),
    NORMAL_OUT("normal_out"),
    START_IN("start_in"),
    START_OUT("start_out")
}