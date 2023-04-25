package com.example.a_sbd.data.mapper

import android.util.Log
import com.example.a_sbd.ui.MainActivity.Companion.TAG
import java.math.RoundingMode
import javax.inject.Inject

class CharacteristicValueMapper @Inject constructor(){
    fun getSeparatedByteArrays(value: ByteArray): List<ByteArray> {
        val separatedByteArray = mutableListOf<ByteArray>()

        Log.d(TAG, "Mapper. Text size: ${value.size}")

        if (value.size < 17) {
            separatedByteArray.add(value)
        } else {
            val blockSize = if (value.size == 19) BLOCK_SIZE_14 else BLOCK_SIZE_16
            val generalByteArray = value
            Log.d(TAG, "bytesize / BLOCK_SIZE = ${generalByteArray.size / BLOCK_SIZE_16.toDouble()}")
            val blocksNumber = ((generalByteArray.size / blockSize.toDouble()).toBigDecimal().setScale(0, RoundingMode.UP)).toInt()
            Log.d(TAG, "Text partition in action. BolocksNumber = $blocksNumber")
            for (i in 0 until blocksNumber) {
                val fromIndex = i * (blockSize + 1)
                val toIndex =
                    if (i < blocksNumber -1) fromIndex + blockSize + 1
                    else generalByteArray.size

                val itemByteArray = generalByteArray.copyOfRange(fromIndex, toIndex)
                separatedByteArray.add(itemByteArray)
            }
        }

        return separatedByteArray
    }

    companion object {
        private const val BLOCK_SIZE_16 = 16
        private const val BLOCK_SIZE_14 = 14
    }
}