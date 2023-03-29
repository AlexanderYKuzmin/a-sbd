package com.example.a_sbd

import android.app.Application
import com.example.a_sbd.di.DaggerASBDComponent

class ASBDApp : Application() {
    val component by lazy {
        DaggerASBDComponent.factory().create(this)
    }
}