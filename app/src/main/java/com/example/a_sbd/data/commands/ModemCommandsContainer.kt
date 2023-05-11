package com.example.a_sbd.data.commands

const val CHECK_MODEM = "AT\r\n"
const val CHECK_SIGNAL_START = "AT+CIER=1,1\r\n"
const val CHECK_SIGNAL_STOP = "AT+CIER=0,0,0,0,0\r\n"
const val GET_SIGNAL_QUALITY_LEVEL = "AT+CSQ"
const val SBDRING_ACTIVATED = "AT+SBDMTA=1\r\n"
const val SBDRING_DISACTIVATED = "AT+SBDMTA=0\r\n"

const val WRITE_TEXT_COMMAND_AND_TEXT_EN = "AT+SBDWT=Once upon a time there was a dear little girl \r\n"
const val WRITE_TEXT_COMMAND = "AT+SBDWT"
const val CLEAR_MO_BUFFER = "AT+SBDD0\r\n"
const val INITIATE_SBD_SESSION = "AT+SBDIX"
const val BUFFER_READ_TEXT = "AT+SBDRT\r\n"
const val BUFFER_READ_BYTES = "AT+SBDRB\r\n"
const val TX_RX_BUFFER = "AT+SBDTC\r\n"
const val RESET_SOFT_DATA = "ATZn\r\n"
const val TRAFFIC_MANAGEMENT_PERIOD = "AT+SBDLOE\r\n"

const val GET_MESSAGE_FROM_SBD = "" //todo

const val WRITE_EMPTY_TO_BUFFER = "AT+SBDWT\r\n"

const val COMMAND_DELIMITER = "="
const val END_OF_COMMAND = "\r\n"