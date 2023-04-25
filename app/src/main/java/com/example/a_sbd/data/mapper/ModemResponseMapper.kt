package com.example.a_sbd.data.mapper

import android.util.Log
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
}