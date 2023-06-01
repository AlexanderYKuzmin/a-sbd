package com.example.a_sbd.data.commands

const val CHECK_MODEM = "AT"
const val CHECK_SIGNAL_START = "AT+CIER"
const val CHECK_SIGNAL_STOP = "AT+CIER"
const val GET_SIGNAL_QUALITY_LEVEL = "AT+CSQ"
const val SBDRING_ACTIVATED = "AT+SBDMTA"
const val SBDRING_DISACTIVATED = "AT+SBDMTA"

const val WRITE_TEXT_COMMAND = "AT+SBDWT"
const val CLEAR_MO_BUFFER = "AT+SBDD0"
const val INITIATE_SBD_SESSION = "AT+SBDIX"
const val BUFFER_READ_TEXT = "AT+SBDRT"
const val BUFFER_READ_BYTES = "AT+SBDRB\r\n"
const val TX_RX_BUFFER = "AT+SBDTC\r\n"
const val RESET_SOFT_DATA = "ATZn\r\n"
const val TRAFFIC_MANAGEMENT_PERIOD = "AT+SBDLOE\r\n"

const val GET_MESSAGE_FROM_SBD = "" //todo

const val WRITE_EMPTY_TO_BUFFER = "AT+SBDWT\r\n"

const val COMMAND_DELIMITER = "="
const val END_OF_COMMAND = "\r\n"