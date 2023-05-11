package com.example.a_sbd.data.mapper

import android.util.Log
import com.example.a_sbd.services.BleService.Companion.MO_MSN
import com.example.a_sbd.services.BleService.Companion.MO_STATUS
import com.example.a_sbd.services.BleService.Companion.MT_MSN
import com.example.a_sbd.services.BleService.Companion.MT_STATUS
import com.example.a_sbd.services.BleService.Companion.QUEUE_LENGTH
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import javax.inject.Inject

class ModemResponseMapper @Inject constructor() {
   fun parseSignalQuality(modemResponseText: String): Int {
       Log.d(TAG, "Parse signal quality response")
       return if (modemResponseText.contains("+CSQ:")) {
           val index = modemResponseText.indexOf(':')
           modemResponseText.substring(index + 1, index + 2).toInt()
       } else {
           throw java.lang.RuntimeException("Wrong modem signal quality response: $modemResponseText")
       }
   }

    fun parseSBDIXResponse(modemResponseText: String): Map<String, Int> {
        Log.d(TAG, "Parse SBDIX parameters: $modemResponseText")
        val sbdixMessageRegex = "(?<=:)[0-9,\\s]+".toRegex()
        val sbdixMessageMatchResult = sbdixMessageRegex.find(modemResponseText)?.value
            ?: throw java.lang.RuntimeException("ERROR of SBDIX status info string")

        //Log.d(TAG, "Parse status message: $sbdixMessageMatchResult")
        val sbdiDataArray = sbdixMessageMatchResult.split(",")
        if (sbdiDataArray.size < 6) throw java.lang.RuntimeException("ERROR of parse SBDI response!")
        return mutableMapOf(
            MO_STATUS to sbdiDataArray[0].trim().toInt(),
            MO_MSN to sbdiDataArray[1].trim().toInt(),
            MT_STATUS to sbdiDataArray[2].trim().toInt(),
            MT_MSN to sbdiDataArray[4].trim().toInt(),
            QUEUE_LENGTH to sbdiDataArray[5].trim().toInt()
        )
    }
}