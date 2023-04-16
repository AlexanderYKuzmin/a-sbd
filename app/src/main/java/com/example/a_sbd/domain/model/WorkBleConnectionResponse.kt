package com.example.a_sbd.domain.model

import android.os.Parcel
import android.os.Parcelable

data class WorkBleConnectionResponse(
    val commandWorker: Int,
    val commandModem: String?,
    val responseWorker: Int,
    val responseModem: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(commandWorker)
        parcel.writeString(commandModem)
        parcel.writeInt(responseWorker)
        parcel.writeString(responseModem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkBleConnectionResponse> {
        override fun createFromParcel(parcel: Parcel): WorkBleConnectionResponse {
            return WorkBleConnectionResponse(parcel)
        }

        override fun newArray(size: Int): Array<WorkBleConnectionResponse?> {
            return arrayOfNulls(size)
        }
    }
}