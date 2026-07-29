package com.babynumbers

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BabyNumbersApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
