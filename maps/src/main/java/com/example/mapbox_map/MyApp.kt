package com.example.mapbox_map

import android.app.Application
import com.example.requestlocation.R
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!MapboxNavigationApp.isSetup()) {
            MapboxNavigationApp.setup {
                NavigationOptions.Builder(applicationContext = applicationContext)
                    .accessToken(R.string.mapbox_access_token.toString())
                    // additional options
                    .build()
            }
        }

    }
}