package com.john.guardian

import android.app.Application
import com.john.guardian.data.AppContainer
import com.john.guardian.data.AppDataContainer

class TheGuardianNewsApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}