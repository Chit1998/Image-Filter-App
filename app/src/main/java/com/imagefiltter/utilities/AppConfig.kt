package com.imagefiltter.utilities

import android.app.Application
import com.imagefiltter.dependencyInjection.RepositoryModule
import com.imagefiltter.dependencyInjection.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(RepositoryModule, ViewModelModule))
        }
    }
}