package com.example.a_sbd.domain.model

import android.os.Parcel
import android.os.Parcelable

data class DeviceSimple(
    val name: String? = "",
    val address: String? = "XX:XX"
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<DeviceSimple> {
            override fun createFromParcel(parcel: Parcel) = DeviceSimple(parcel)
            override fun newArray(size: Int) = arrayOfNulls<DeviceSimple>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        name = parcel.readString(),
        address = parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
    }

    override fun describeContents() = 0
}
