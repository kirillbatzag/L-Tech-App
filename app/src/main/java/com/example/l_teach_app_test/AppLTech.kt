package com.example.l_teach_app_test

import android.app.Application
import networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import repositoryModule
import useCaseModule
import viewModelModule

class AppLTech : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppLTech)
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}
